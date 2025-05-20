package br.com.solari.infrastructure.mapper;

import br.com.solari.application.domain.Order;
import br.com.solari.application.domain.Orderstatus;
import br.com.solari.infrastructure.persistence.entity.OrderEntity;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class OrderMapperTest {

    @Test
    void shouldMapToEntity() {
        Order order = Order.builder()
                .id("1")
                .orderStatus(Orderstatus.ABERTO)
                .products(Map.of("12345", 2))
                .cpf("12345678900")
                .paymentType("CREDIT_CARD")
                .paymentIdentification("ABC123")
                .build();

        OrderEntity entity = OrderMapper.toEntity(order);

        assertEquals(order.getId(), entity.getId());
        assertEquals(order.getOrderStatus(), entity.getOrderStatus());
        assertEquals(order.getProducts(), entity.getProducts());
        assertEquals(order.getCpf(), entity.getCpf());
        assertEquals(order.getPaymentType(), entity.getPaymentType());
        assertEquals(order.getPaymentIdentification(), entity.getPaymentIdentification());
        assertNotNull(entity.getCreatedDate());
        assertNotNull(entity.getLastModifiedDate());
    }

    @Test
    void shouldMapToDomain() {
        OrderEntity entity = OrderEntity.builder()
                .id("1")
                .orderStatus(Orderstatus.ABERTO)
                .products(Map.of("12345", 2))
                .cpf("12345678900")
                .paymentType("CREDIT_CARD")
                .paymentIdentification("ABC123")
                .build();

        Order order = OrderMapper.toDomain(entity);

        assertEquals(entity.getId(), order.getId());
        assertEquals(entity.getOrderStatus(), order.getOrderStatus());
        assertEquals(entity.getProducts(), order.getProducts());
        assertEquals(entity.getCpf(), order.getCpf());
        assertEquals(entity.getPaymentType(), order.getPaymentType());
        assertEquals(entity.getPaymentIdentification(), order.getPaymentIdentification());
    }
}
