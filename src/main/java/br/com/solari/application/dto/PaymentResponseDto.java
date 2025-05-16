package br.com.solari.application.dto;

import br.com.solari.infrastructure.event.PaymentMethod;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponseDto {

    @NotBlank(message = "Payment id is required")
    private String id;

    @NotBlank(message = "Payment token is required")
    private String token;

    @NotBlank(message = "statusis required")
    private String status;

    @NotBlank(message = "PaymentMethod is required")
    private PaymentMethod method;
}