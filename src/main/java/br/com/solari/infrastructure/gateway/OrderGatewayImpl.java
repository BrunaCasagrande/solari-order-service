package br.com.solari.infrastructure.gateway;

import br.com.solari.application.domain.Order;
import br.com.solari.application.domain.Orderstatus;
import br.com.solari.application.domain.exception.OrderNotFoundException;
import br.com.solari.application.gateway.OrderGateway;
import br.com.solari.infrastructure.event.OrderEvent;
import br.com.solari.infrastructure.mapper.OrderMapper;
import br.com.solari.infrastructure.persistence.entity.OrderEntity;
import br.com.solari.infrastructure.persistence.repository.OrderRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderGatewayImpl implements OrderGateway {

  private final OrderRepository orderRepository;

  public void saveOrder(OrderEvent orderEvent) {

    Order order =
        Order.createOrder(
            orderEvent.getId(),
            orderEvent.getProducts(),
            orderEvent.getCpf(),
            String.valueOf(orderEvent.getPaymentData().getPaymentMethod()),
            orderEvent.getPaymentData().getCreditCardNumber());

    OrderEntity orderEntity = OrderMapper.toEntity(order);
    orderRepository.save(orderEntity);
  }

  public void updateOrder(OrderEvent orderEvent, Orderstatus status) {

    Order order =
        Order.createOrder(
            orderEvent.getId(),
            orderEvent.getProducts(),
            orderEvent.getCpf(),
            String.valueOf(orderEvent.getPaymentData().getPaymentMethod()),
            orderEvent.getPaymentData().getCreditCardNumber());

    OrderEntity orderEntity = OrderMapper.toEntity(order);
    orderEntity.setOrderStatus(status);

    orderRepository.save(orderEntity);
  }

  public Optional<Order> getOrder(String id) {

    OrderEntity orderEntity =
        orderRepository
            .findById(id)
            .orElseThrow(() -> new OrderNotFoundException(id + " not found"));
    return Optional.of(OrderMapper.toDomain(orderEntity));
  }
}
