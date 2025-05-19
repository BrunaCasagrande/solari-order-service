package br.com.solari.application.usecase;

import br.com.solari.application.domain.Order;
import br.com.solari.application.gateway.OrderGateway;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SearchOrder {

  private final OrderGateway orderGateway;

  public Optional<Order> getOrder(String id) {
    return orderGateway.getOrder(id);
  }
}
