package br.com.solari.infrastructure.event;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Map;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderEvent {

  private String id;
  private Map<String, Integer> products;
  private String cpf;
  private PaymentData paymentData;
}
