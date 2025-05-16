package br.com.solari.infrastructure.gateway;

import static br.com.solari.fixture.OrderFixture.createOrderEntityExemple;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import br.com.solari.infrastructure.persistence.repository.OrderRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;

public class OrderGatewayImplTest {

  private final OrderRepository orderRepository = mock(OrderRepository.class);
  private final OrderGatewayImpl userGateway = new OrderGatewayImpl(orderRepository);

  @Test
  void shouldFindUserByCpfSuccessfully() {

    final var entity = createOrderEntityExemple();
    when(orderRepository.findById(entity.getId())).thenReturn(Optional.of(entity));

    final var response = userGateway.getOrder(entity.getId());

    assertThat(response).isPresent();
    assertThat(response)
        .hasValueSatisfying(
            order -> {
              assertThat(order.getId()).isEqualTo(entity.getId());
              assertThat(order.getOrderStatus()).isEqualTo(entity.getOrderStatus());
              assertThat(order.getCpf()).isEqualTo(entity.getCpf());
              assertThat(order.getPaymentType()).isEqualTo(entity.getPaymentType());
              assertThat(order.getPaymentIdentification())
                  .isEqualTo(entity.getPaymentIdentification());
            });

    verify(orderRepository).findById(entity.getId());
  }
}
