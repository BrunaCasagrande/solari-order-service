package br.com.solari.application.domain;

import br.com.solari.application.domain.exception.DomainException;
import br.com.solari.application.domain.exception.ErrorDetail;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Order {

  @NotBlank(message = "id is required")
  private String id;

  @NotNull(message = "orderStatus is required")
  private Orderstatus orderStatus;

  @NotNull(message = "products is required")
  private Map<String, Integer> products;

  @NotBlank(message = "cpf is required")
  private String cpf;

  @NotBlank(message = "paymentData is required")
  private String paymentType;

  @NotBlank(message = "paymentIdentification is required")
  private String paymentIdentification;

  public static Order createOrder(
      final String id,
      final Map<String, Integer> products,
      final String cpf,
      final String paymentType,
      final String paymentIdentification) {

    final var order =
        Order.builder()
            .id(id)
            .orderStatus(Orderstatus.ABERTO)
            .products(products)
            .cpf(cpf)
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
      final List<ErrorDetail> errors =
          violations.stream()
              .map(
                  violation ->
                      new ErrorDetail(
                          violation.getPropertyPath().toString(),
                          violation.getMessage(),
                          violation.getInvalidValue()))
              .toList();

      final String firstErrorMessage = errors.get(0).errorMessage();

      throw new DomainException(firstErrorMessage, "domain_exception", errors);
    }
  }
}
