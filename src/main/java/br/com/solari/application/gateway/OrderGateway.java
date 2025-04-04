package br.com.solari.application.gateway;

import br.com.solari.application.domain.Order;
import br.com.solari.application.domain.Orderstatus;
import br.com.solari.infrastructure.event.OrderEvent;
import br.com.solari.infrastructure.presenter.dto.ClientDTO;

import java.util.Optional;

public interface OrderGateway {

    void updateOrder(OrderEvent orderEvent, Orderstatus status);
    void saveOrder(OrderEvent orderEvent);
    Optional<Order> getOrder(String id);
    Optional<ClientDTO> getClientFromClientService(String cpf);
}
