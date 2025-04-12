package br.com.solari.application.usecase;

import br.com.solari.application.domain.Order;
import br.com.solari.application.gateway.OrderGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SearchOrder {

    private final OrderGateway orderGateway;

    public Optional<Order> getOrder(String id) {
        return orderGateway.getOrder(id);
    }
}
