package br.com.solari.infrastructure.rest;

import br.com.solari.infrastructure.presenter.dto.ClientDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;

import java.net.ConnectException;
@Slf4j
@Component
public class ExternalServiceClient {

    private final RestTemplate restTemplate;

    @Value("${rest.url.basepath.client}")
    private String basePath;

    public ExternalServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Retry(name = "retryClientService", fallbackMethod = "fallbackClient")
    @CircuitBreaker(name = "circuitBrakerClientService")
    public ClientDTO getClient(String cpf) {
        log.info("Realizando chamada de  serviço externo: {}", basePath);
        String url = basePath + cpf;
        return restTemplate.getForObject(url, ClientDTO.class);
    }

    public ClientDTO fallbackClient(String cpf, Throwable throwable) {
        log.info("Class Name Fallback Error: {}",  throwable.getClass().getName());
        throw new RuntimeException("Erro no fallback para CPF " + cpf, throwable);
    }
}