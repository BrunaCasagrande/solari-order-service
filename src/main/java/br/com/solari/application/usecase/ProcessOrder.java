package br.com.solari.application.usecase;

import br.com.solari.application.domain.Orderstatus;
import br.com.solari.application.dto.InventoryDTO;
import br.com.solari.application.dto.ProductDTO;
import br.com.solari.application.gateway.OrchestrationGateway;
import br.com.solari.application.gateway.OrderGateway;
import br.com.solari.infrastructure.event.OrderEvent;
import br.com.solari.application.dto.ClientDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProcessOrder {

    private final OrderGateway orderGateway;

    private final OrchestrationGateway orchestrationGateway;

    public void processOrder(OrderEvent orderEvent) {
        orderGateway.saveOrder(orderEvent);
        try {
            // Realiza o começo da orquestração chamando o serviço de cliente
            Optional<ClientDTO> client = orchestrationGateway.getClientFromClientService(orderEvent.getCpf());
            if (client.isPresent()) {
                log.info("Cliente obtido -> cpf: {}", client.get().getCpf());
                log.info("Cliente obtido -> : {}", client.get().toString());
            } else {
                log.warn("Cliente não encontrado para o CPF: {}", orderEvent.getCpf());
                throw new IllegalStateException("Cliente não encontrado");
            }

            // Chamada do produto (get solari-product-service)
            Optional<ProductDTO> product = orchestrationGateway.getProductFromProductService(orderEvent.getProducts().keySet().iterator().next());
            if (product.isPresent()) {
                log.info("Produto obtido -> sku: {}", product.get().getSku());
                log.info("Produto obtido -> : {}", product.get().toString());
            } else {
                log.warn("Produto não encontrado para o SKU: {}", orderEvent.getProducts().keySet().iterator().next());
                throw new IllegalStateException("Produto não encontrado");
            }

            // Chamada do estoque (get solari-inventory-service)
            Optional<List<InventoryDTO>> inventoryList = orchestrationGateway.getInventoryFromInventoryService(product.get().getSku());
            if (inventoryList.isPresent() && !inventoryList.get().isEmpty()) {
                log.info("Estoque obtido -> sku: {}", product.get().getSku());
                inventoryList.get().forEach(inventory -> log.info("Estoque -> {}", inventory.toString()));
            } else {
                log.warn("Estoque não encontrado para o SKU: {}", product.get().getSku());
                throw new IllegalStateException("Estoque não encontrado");
            }
            //Precisa atualizar o estoque (update solari-inventory-service)

            //Precisa chamar o pagamento (post solari-payment-service)
        } catch (Exception e) {
            log.error("Exceção capturada em processOrder: {}", e.getMessage(), e);
            log.info("Marcando pedido para reprocessamento:  {}", orderEvent.getId());
            orderGateway.updateOrder(orderEvent, Orderstatus.ERRO);
        }
    }
}
