package br.com.solari.fixture;

import br.com.solari.application.domain.Order;
import br.com.solari.infrastructure.event.OrderEvent;
import br.com.solari.infrastructure.event.PaymentData;
import br.com.solari.infrastructure.event.PaymentMethod;
import br.com.solari.infrastructure.presenter.dto.AddressDTO;
import br.com.solari.infrastructure.presenter.dto.ClientDTO;

import java.util.Map;

public final class OrderFixture {

    public static final String ID = "3b035f13-80d0-4af0-9f27-5314f8ad8a8d";
    public static final String CPF = "12345678900";
    public static final PaymentData PAYMENT_DATA = new PaymentData(PaymentMethod.CREDIT_CARD, "123456789");
    public static final Map<Integer, Integer> PRODUCTS = Map.of(
            1, 2,
            2, 3
    );

    private OrderFixture() {}

    public static Order existingOrder() {
        return Order.createOrder(
                ID,
                PRODUCTS,
                CPF,
                "CREDIT_CARD",
                "987654321987654312"
        );
    }

    public static OrderEvent existingOrderEvent() {
        return new OrderEvent(
                ID,
                PRODUCTS,
                CPF,
                PAYMENT_DATA
        );
    }
    public static ClientDTO existingClientDTO() {
        AddressDTO AddressDTO = new AddressDTO(1,
                "rua teste",
                "1",
                "São Paulo",
                "São Paulo",
                "03966100");

        return new ClientDTO(
                1,
                "Bruna Teste",
                CPF,
                "11 977785522",
                "teste@gmail.com",
                AddressDTO
        );
    }
}