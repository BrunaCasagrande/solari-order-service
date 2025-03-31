package br.com.solari.application.usecase;

import br.com.solari.application.gateway.OrderGateway;
import br.com.solari.infrastructure.event.OrderEvent;
import br.com.solari.infrastructure.presenter.dto.ClientDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProcessOrderUseCase {

    private final OrderGateway orderGateway;

    public void processOrder(OrderEvent orderEvent) {
        orderGateway.saveOrder(orderEvent);

        Optional<ClientDTO> client = orderGateway.getClientFromClientService(orderEvent.getCpf());
        System.out.println("###Cliente obtido: " + client.toString());
    }
}
