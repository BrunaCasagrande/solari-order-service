package br.com.solari.infrastructure.presenter;

import br.com.solari.application.domain.Order;
import br.com.solari.application.domain.Orderstatus;
import br.com.solari.infrastructure.presenter.response.OrderPresenterResponse;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class OrderPresenterTest {

    private final OrderPresenter presenter = new OrderPresenter();

    @Test
    void shouldParseOrderToResponseSuccessfully() {
        Order order = Order.builder()
                .id("1")
                .orderStatus(Orderstatus.FECHADO_COM_SUCESSO)
                .products(Map.of("12345", 2))
                .cpf("12345678900")
                .paymentType("CREDIT_CARD")
                .paymentIdentification("1234-5678-9012-3456")
                .build();

        OrderPresenterResponse response = presenter.parseToResponse(order);

        assertEquals("1", response.id());
        assertEquals("FECHADO_COM_SUCESSO", response.status());
        assertEquals("12345678900", response.cpf());
        assertEquals(2, response.products().get("12345"));
    }
}
