package br.com.solari.application.gateway;

import br.com.solari.application.domain.Order;
import br.com.solari.infrastructure.event.OrderEvent;

public interface OrderGateway {

    void saveOrder(OrderEvent orderEvent);
    Order getOrder(String id);
}
