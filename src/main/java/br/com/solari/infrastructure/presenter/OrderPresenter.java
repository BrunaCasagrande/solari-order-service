package br.com.solari.infrastructure.presenter;
import br.com.solari.application.domain.Order;
import br.com.solari.infrastructure.presenter.response.OrderPresenterResponse;
import org.springframework.stereotype.Component;

@Component
public class OrderPresenter {

    public OrderPresenterResponse parseToResponse(final Order order) {
        return OrderPresenterResponse.builder()
                .id(order.getId())
                .status(order.getOrderStatus().toString())
                .products(order.getProducts())
                .cpf(order.getCpf())
        .build();
    }
}