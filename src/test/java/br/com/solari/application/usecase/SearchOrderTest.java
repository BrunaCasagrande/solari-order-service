package br.com.solari.application.usecase;

import static br.com.solari.fixture.OrderFixture.ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

import br.com.solari.application.domain.exception.OrderNotFoundException;
import br.com.solari.application.gateway.OrderGateway;
import br.com.solari.fixture.OrderFixture;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class SearchOrderTest {

  private final OrderGateway orderGateway = mock(OrderGateway.class);

  private final SearchOrder searchOrder = new SearchOrder(orderGateway);

  @Test
  void shouldFindOrderSuccessfullyById() {

    final var order = OrderFixture.createOrderExemple();
    when(orderGateway.getOrder(order.getId())).thenReturn(Optional.of(order));

    final var result = searchOrder.getOrder(order.getId());

    assertThat(result).isPresent();
    assertThat(result).hasValue(order);

    verify(orderGateway).getOrder(order.getId());
  }

  @Test
  void shouldThrowExceptionWhenOrderEmpty() {

    when(orderGateway.getOrder(ID)).thenReturn(Optional.empty());

    final var response = searchOrder.getOrder(ID);

    assertThat(response).isEmpty();

    verify(orderGateway).getOrder(ID);
  }

  @Test
  void shouldThrowExceptionWhenOrderNotFoundById() {

    when(orderGateway.getOrder(ID)).thenThrow(new OrderNotFoundException(ID + " not found"));

    assertThatThrownBy(() -> searchOrder.getOrder(ID))
        .isInstanceOf(OrderNotFoundException.class)
        .hasMessage("Order with ID=[" + ID + " not found] not found.");

    verify(orderGateway, times(1)).getOrder(ID);
  }
}
