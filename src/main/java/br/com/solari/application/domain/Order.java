package br.com.solari.application.domain;

import br.com.solari.infrastructure.config.exception.GatewayException;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Order {

    @NotNull(message = "id is required")
    private String id;

    @NotNull(message = "orderStatus is required")
    private Orderstatus orderStatus;

    @NotNull(message = "products is required")
    private Map<Integer, Integer> products;

    @NotNull(message = "clientId is required")
    private Integer clientId;

    @NotNull(message = "paymentData is required")
    private String paymentType;

    @NotNull(message = "paymentIdentification is required")
    private String paymentIdentification;

    public static Order createOrder(
            final String id,
            final Map<Integer, Integer> products,
            final Integer clientId,
            final String paymentType,
            final String paymentIdentification
            ) {

        final var order =
                Order.builder()
                        .id(id)
                        .orderStatus(Orderstatus.ABERTO)
                        .products(products)
                        .clientId(clientId)
                        .paymentType(paymentType)
                        .paymentIdentification(paymentIdentification)
                        .build();

        validate(order);

        return order;
    }

    private static void validate(final Order order) {
        final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        final Validator validator = factory.getValidator();
        final Set<ConstraintViolation<Order>> violations = validator.validate(order);

        if (!violations.isEmpty()) {
            final List<GatewayException.ErrorDetail> errors =
                    violations.stream()
                            .map(
                                    violation ->
                                            new GatewayException.ErrorDetail(
                                                    violation.getPropertyPath().toString(),
                                                    violation.getMessage(),
                                                    violation.getInvalidValue()))
                            .toList();

            final String firstErrorMessage = errors.get(0).errorMessage();

            throw new GatewayException.DomainException(firstErrorMessage, "domain_exception", errors);
        }
    }
}
