package br.com.solari.infrastructure.rest;

import br.com.solari.application.dto.PaymentRequestDto;
import br.com.solari.application.dto.PaymentResponseDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class ExternalServicePayment {

    private final RestTemplate restTemplate;

    @Value("${rest.url.basepath.payment}")
    private String basePath;

    public ExternalServicePayment(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Retry(name = "retryPaymentService", fallbackMethod = "fallbackPayment")
    @CircuitBreaker(name = "circuitBreakerPaymentService")
    public PaymentResponseDto processPayment(String token) {
        log.info("Realizando chamada para o serviço de pagamento: {}", basePath);
        String url = basePath;
        PaymentRequestDto payment = new PaymentRequestDto();
        payment.setToken(token);
        return restTemplate.postForObject(url, payment, PaymentResponseDto.class);
    }

    public void fallbackPayment(PaymentRequestDto paymentDTO, Throwable throwable) {
        log.error("Erro ao processar pagamento. Fallback acionado: {}", throwable.getMessage());
        throw new RuntimeException("Erro no fallback do serviço de pagamento", throwable);
    }
}