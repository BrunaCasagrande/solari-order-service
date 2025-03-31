package br.com.solari.infrastructure.config;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.net.ConnectException;
import java.time.Duration;
import java.util.concurrent.TimeoutException;

@Configuration
public class ResilienceConfiguration {

    @Bean
    public CircuitBreakerRegistry circuitBreakerRegistry() {
        CircuitBreakerConfig config = CircuitBreakerConfig.custom()
                .failureRateThreshold(50) // % de falhas para abrir o circuito
                .waitDurationInOpenState(Duration.ofSeconds(10)) // Tempo aberto antes de reavaliar
                .slidingWindowSize(5) // Quantidade de chamadas para calcular taxa de falha
                .recordExceptions(ConnectException.class) // Tipos de exceções que contam como falha
                .build();

        return CircuitBreakerRegistry.of(config);
    }

    @Bean
    public RetryRegistry retryRegistry() {
        RetryConfig config = RetryConfig.custom()
                .maxAttempts(3) // Número máximo de tentativas
                .waitDuration(Duration.ofMillis(500)) // Intervalo entre tentativas
                .retryExceptions(ResourceAccessException.class, ConnectException.class, TimeoutException.class)
                .build();

        return RetryRegistry.of(config);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}