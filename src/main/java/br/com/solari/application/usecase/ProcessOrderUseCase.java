package br.com.solari.application.usecase;

import br.com.solari.application.gateway.OrderGateway;
import br.com.solari.infrastructure.event.OrderEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProcessOrderUseCase {

    private final OrderGateway orderGateway;

    public void processOrder(OrderEvent orderEvent) {
        orderGateway.saveOrder(orderEvent);
    }
}
