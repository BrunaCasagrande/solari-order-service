package br.com.solari.infrastructure.mapper;

import br.com.solari.application.domain.Order;
import br.com.solari.infrastructure.persistence.entity.OrderEntity;
import java.time.LocalDateTime;

public class OrderMapper {

  public static OrderEntity toEntity(Order order) {
    return OrderEntity.builder()
        .id(order.getId())
        .orderStatus(order.getOrderStatus())
        .products(order.getProducts())
        .cpf(order.getCpf())
        .paymentType(order.getPaymentType())
        .paymentIdentification(order.getPaymentIdentification())
        .createdDate(LocalDateTime.now())
        .lastModifiedDate(LocalDateTime.now())
        .build();
  }

  public static Order toDomain(OrderEntity orderEntity) {
    return Order.builder()
        .id(orderEntity.getId())
        .orderStatus(orderEntity.getOrderStatus())
        .products(orderEntity.getProducts())
        .cpf(orderEntity.getCpf())
        .paymentType(orderEntity.getPaymentType())
        .paymentIdentification(orderEntity.getPaymentIdentification())
        .build();
  }
}
