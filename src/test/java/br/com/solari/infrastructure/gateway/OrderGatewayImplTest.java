package br.com.solari.infrastructure.gateway;

import static br.com.solari.fixture.OrderFixture.createOrderEntityExemple;
import static br.com.solari.fixture.OrderFixture.createOrderEventExemple;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import br.com.solari.application.domain.Orderstatus;
import br.com.solari.infrastructure.event.OrderEvent;
import br.com.solari.infrastructure.persistence.repository.OrderRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class OrderGatewayImplTest {

    private OrderRepository orderRepository;
    private OrderGatewayImpl orderGateway;

    @BeforeEach
    void setup() {
        orderRepository = mock(OrderRepository.class);
        orderGateway = new OrderGatewayImpl(orderRepository);
    }

    @Test
    void shouldSaveOrderSuccessfully() {
        OrderEvent orderEvent = createOrderEventExemple();

        orderGateway.saveOrder(orderEvent);

        verify(orderRepository, times(1)).save(any());
    }

    @Test
    void shouldUpdateOrderSuccessfully() {
        OrderEvent orderEvent = createOrderEventExemple();

        orderGateway.updateOrder(orderEvent, Orderstatus.FECHADO_COM_SUCESSO);

        verify(orderRepository, times(1)).save(argThat(orderEntity ->
                orderEntity.getOrderStatus().equals(Orderstatus.FECHADO_COM_SUCESSO)
        ));
    }

    @Test
    void shouldFindOrderByIdSuccessfully() {
        final var entity = createOrderEntityExemple();
        when(orderRepository.findById(entity.getId())).thenReturn(Optional.of(entity));

        final var response = orderGateway.getOrder(entity.getId());

        assertThat(response).isPresent();
        assertThat(response)
                .hasValueSatisfying(order -> {
                    assertThat(order.getId()).isEqualTo(entity.getId());
                    assertThat(order.getOrderStatus()).isEqualTo(entity.getOrderStatus());
                    assertThat(order.getCpf()).isEqualTo(entity.getCpf());
                    assertThat(order.getPaymentType()).isEqualTo(entity.getPaymentType());
                    assertThat(order.getPaymentIdentification()).isEqualTo(entity.getPaymentIdentification());
                });

        verify(orderRepository).findById(entity.getId());
    }
}
