package br.com.solari.infrastructure.event;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentData {
  private PaymentMethod paymentMethod;
  private String creditCardNumber;
}
