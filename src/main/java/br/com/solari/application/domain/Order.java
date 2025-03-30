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
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Order {

  private String id;

  @NotNull(message = "products is required")
  private Map<Integer, Integer> products;

  @NotNull(message = "clientId is required")
  private Integer clientId;

  @NotNull(message = "paymentData is required")
  private PaymentData paymentData;

  public static Order createOrder(
          final Map<Integer, Integer> products,
          final Integer clientId,
          final PaymentData paymentData) {

    final var order =
            Order.builder()
                    .id(UUID.randomUUID().toString())
                    .products(products)
                    .clientId(clientId)
                    .paymentData(paymentData)
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
