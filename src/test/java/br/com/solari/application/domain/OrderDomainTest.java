package br.com.solari.application.domain;

import br.com.solari.application.domain.exception.DomainException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Map;

import static br.com.solari.fixture.OrderFixture.PRODUCTS;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class OrderDomainTest {

    @Test
    void shouldCreateOrderWithSuccess() {

        final Order order = Order.createOrder(
                "3b035f13-80d0-4af0-9f27-5314f8ad8a8d",
                PRODUCTS,
                "12345678900",
                "CREDIT_CARD",
                "987654321987654312"
        );

        assertThat(order.getId()).isEqualTo("3b035f13-80d0-4af0-9f27-5314f8ad8a8d");
        assertThat(order.getProducts()).isEqualTo(PRODUCTS);
        assertThat(order.getCpf()).isEqualTo("12345678900");
        assertThat(order.getPaymentType()).isEqualTo("CREDIT_CARD");
        assertThat(order.getPaymentIdentification()).isEqualTo("987654321987654312");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" "})
    void shouldNotCreateWhenIdIsInvalid(final String emptyId) {
        assertThatThrownBy(
                () ->
                        Order.createOrder(
                                emptyId, PRODUCTS, "12345678900",
                                "CREDIT_CARD",
                                "987654321987654312"))
                .isInstanceOf(DomainException.class)
                .hasMessage("id is required");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" "})
    void shouldNotCreateWhenCpfIsInvalid(final String invalidCpf) {
        assertThatThrownBy(
                () -> Order.createOrder(
                        "3b035f13-80d0-4af0-9f27-5314f8ad8a8d", PRODUCTS, invalidCpf,
                        "CREDIT_CARD",
                        "987654321987654312"))
                .isInstanceOf(DomainException.class)
                .hasMessage("cpf is required");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" "})
    void shouldNotCreateWhenPaymentTypeIsInvalid(final String invalidPaymentType) {
        assertThatThrownBy(
                () -> Order.createOrder(
                        "3b035f13-80d0-4af0-9f27-5314f8ad8a8d", PRODUCTS, "12345678900",
                        invalidPaymentType,
                        "987654321987654312"))
                .isInstanceOf(DomainException.class)
                .hasMessage("paymentData is required");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" "})
    void shouldNotCreateWhenPaymentIdentificationIsInvalid(final String invalidPaymentIdentification) {
        assertThatThrownBy(
                () -> Order.createOrder(
                        "3b035f13-80d0-4af0-9f27-5314f8ad8a8d", PRODUCTS, "12345678900",
                        "CREDIT_CARD",
                        invalidPaymentIdentification))
                .isInstanceOf(DomainException.class)
                .hasMessage("paymentIdentification is required");
    }
}
