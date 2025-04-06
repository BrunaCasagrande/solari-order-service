package br.com.solari.application.usecase;

import br.com.solari.application.domain.Orderstatus;
import br.com.solari.application.gateway.OrchestrationGateway;
import br.com.solari.application.gateway.OrderGateway;
import br.com.solari.infrastructure.event.OrderEvent;
import br.com.solari.infrastructure.presenter.dto.ClientDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProcessOrderUseCase {

    private final OrderGateway orderGateway;

    private final OrchestrationGateway orchestrationGateway;

    public void processOrder(OrderEvent orderEvent) {
        orderGateway.saveOrder(orderEvent);
        try {
            //Realiza o começo da orquestracao chamando o servico de cliente
            Optional<ClientDTO> client = orchestrationGateway.getClientFromClientService(orderEvent.getCpf());
            log.info("Cliente obtido -> cpf: {}", client.get().getCpf());
            // Precisa implementar a chamada do produto (get solari-inventory-service)

            //Precisa implementar a chamada do estoque (get solari-inventory-service)

            //Precisa atualizar o estoque (update solari-inventory-service)

            //Precisa chamar o pagamento (post solari-payment-service)
        } catch (Exception e) {
            log.error("Exceção capturada em processOrder: {}", e.getMessage(), e);
            log.info("Marcando pedido para reprocessamento:  {}", orderEvent.getId());
            orderGateway.updateOrder(orderEvent, Orderstatus.ERRO);
        }
    }
}
