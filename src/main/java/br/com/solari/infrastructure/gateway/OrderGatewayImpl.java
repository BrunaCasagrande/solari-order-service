package br.com.solari.infrastructure.gateway;

import br.com.solari.application.domain.Order;
import br.com.solari.application.domain.exception.OrderNotFoundException;
import br.com.solari.application.gateway.OrderGateway;
import br.com.solari.infrastructure.event.OrderEvent;
import br.com.solari.infrastructure.mapper.OrderMapper;
import br.com.solari.infrastructure.persistence.entity.OrderEntity;
import br.com.solari.infrastructure.persistence.repository.OrderRepository;
import br.com.solari.infrastructure.presenter.dto.ClientDTO;
import br.com.solari.infrastructure.rest.ExternalServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OrderGatewayImpl implements OrderGateway {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ExternalServiceClient externalServiceClient;

    public void saveOrder(OrderEvent orderEvent) {

        Order order = Order.createOrder(
                orderEvent.getId(),
                orderEvent.getProducts(),
                orderEvent.getCpf(),
                String.valueOf(orderEvent.getPaymentData().getPaymentMethod()),
                orderEvent.getPaymentData().getCreditCardNumber());

        OrderEntity orderEntity = OrderMapper.toEntity(order);
        orderRepository.save(orderEntity);

    }

    public Optional<Order> getOrder(String id) {

        OrderEntity orderEntity = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));
        return Optional.of(OrderMapper.toDomain(orderEntity));

    }

    public Optional<ClientDTO> getClientFromClientService(String cpf){
        return Optional.ofNullable(externalServiceClient.getClient(cpf));
    }
}
