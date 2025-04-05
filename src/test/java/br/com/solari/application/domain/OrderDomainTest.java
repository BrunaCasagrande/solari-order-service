package br.com.solari.application.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import java.util.Map;

public class OrderDomainTest {

    @Test
    void shouldCreateOrderWithSuccess() {

        Map<Integer, Integer> products = Map.of(
                1, 2,
                2, 3
        );

        final Order order = Order.createOrder(
                "3b035f13-80d0-4af0-9f27-5314f8ad8a8d",
                products,
                "12345678900",
                "CREDIT_CARD",
                "987654321987654312"
        );

        assertThat(order.getId()).isEqualTo("3b035f13-80d0-4af0-9f27-5314f8ad8a8d");
        assertThat(order.getProducts()).isEqualTo(products);
        assertThat(order.getCpf()).isEqualTo("12345678900");
        assertThat(order.getPaymentType()).isEqualTo("CREDIT_CARD");
        assertThat(order.getPaymentIdentification()).isEqualTo("987654321987654312");
    }

}
