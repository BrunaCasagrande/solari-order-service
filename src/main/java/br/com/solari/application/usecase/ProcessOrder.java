package br.com.solari.application.usecase;

import br.com.solari.application.domain.Orderstatus;
import br.com.solari.application.dto.*;
import br.com.solari.application.gateway.OrchestrationGateway;
import br.com.solari.application.gateway.OrderGateway;
import br.com.solari.infrastructure.event.OrderEvent;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import br.com.solari.infrastructure.event.PaymentData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProcessOrder {

  private final OrderGateway orderGateway;

  private final OrchestrationGateway orchestrationGateway;

  public void processOrder(OrderEvent orderEvent) {
    orderGateway.saveOrder(orderEvent);
    try {
      Optional<ClientDTO> client =
          orchestrationGateway.getClientFromClientService(orderEvent.getCpf());
      if (client.isPresent()) {
        log.info("Cliente obtido -> cpf: {}", client.get().getCpf());
        log.info("Cliente obtido -> : {}", client.get().toString());
      } else {
        log.warn("Cliente não encontrado para o CPF: {}", orderEvent.getCpf());
        throw new IllegalStateException("Cliente não encontrado");
      }

      for (Map.Entry<String, Integer> entry : orderEvent.getProducts().entrySet()) {
        String sku = entry.getKey();
        int orderQuantity = entry.getValue();

        // Buscar produto
        Optional<ProductDTO> product = orchestrationGateway.getProductFromProductService(sku);
        if (product.isPresent()) {
          log.info("Produto obtido -> sku: {}", sku);
          log.info("Produto obtido -> : {}", product.get().toString());
        } else {
          log.warn("Produto não encontrado para o SKU: {}", sku);
          throw new IllegalStateException("Produto não encontrado");
        }

        // Buscar estoque
        Optional<List<InventoryDTO>> inventoryList = orchestrationGateway.getInventoryFromInventoryService(sku);
        if (inventoryList.isPresent() && !inventoryList.get().isEmpty()) {
          log.info("Estoque obtido -> SKU: {}", sku);
          InventoryDTO currentInventory = inventoryList.get().getFirst();

          int currentQuantity = currentInventory.getQuantity();
          if (orderQuantity > currentQuantity) {
            log.error(
                    "Quantidade insuficiente no estoque para o SKU [{}]. Requisitado: {}, Disponível: {}",
                    sku,
                    orderQuantity,
                    currentQuantity
            );
            throw new IllegalStateException("Quantidade insuficiente no estoque para o SKU: " + sku);
          }

          InventoryDTO updatedInventory = new InventoryDTO();
          updatedInventory.setSku(sku);
          updatedInventory.setQuantity(-orderQuantity); // subtração será feita no serviço

          orchestrationGateway.updateInventoryInInventoryService(sku, updatedInventory);
          log.info("Estoque atualizado para o SKU [{}]. Subtraído: {}", sku, orderQuantity);
        } else {
          log.warn("Estoque não encontrado para o SKU [{}]", sku);
          throw new IllegalStateException("Estoque não encontrado para o SKU: " + sku);
        }
      }

      String paymentToken = createPaymentToken(orderEvent.getPaymentData());

      if (paymentToken.isBlank()) {
        throw new IllegalArgumentException("Token de pagamento ausente no pedido.");
      }
      log.info("Enviando pagamento com token: {}", paymentToken);
      PaymentResponseDto paymentResponse = orchestrationGateway.processPayment(paymentToken);
      log.info("Pagamento processado. Status: {}", paymentResponse.getStatus());
      orderGateway.updateOrder(orderEvent, Orderstatus.FECHADO_COM_SUCESSO);
      if ("APPROVED".equalsIgnoreCase(paymentResponse.getStatus())) {
        orderGateway.updateOrder(orderEvent, Orderstatus.FECHADO_COM_SUCESSO);
      } else {
        orderGateway.updateOrder(orderEvent, Orderstatus.FECHADO_SEM_CREDITO);
      }


    } catch (Exception e) {
      log.error("Exceção capturada em processOrder: {}", e.getMessage(), e);
      log.info("Marcando pedido para reprocessamento:  {}", orderEvent.getId());
      orderGateway.updateOrder(orderEvent, Orderstatus.ERRO);
    }
  }

  private String createPaymentToken(PaymentData data) {
    return data.getCreditCardNumber() + data.getPaymentMethod() + "addTokenizerRule";
  }
}
