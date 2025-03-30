package br.com.solari.application.usecase;

import br.com.solari.application.domain.Order;
import br.com.solari.application.gateway.OrderGateway;
import br.com.solari.infrastructure.event.OrderEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SearchOrderUseCase {

    private final OrderGateway orderGateway;

    public Order getOrder(String id) {
        return orderGateway.getOrder(id);
    }
}
