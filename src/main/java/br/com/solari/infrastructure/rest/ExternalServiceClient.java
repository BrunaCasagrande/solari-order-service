package br.com.solari.infrastructure.rest;

import br.com.solari.infrastructure.presenter.dto.ClientDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;

import java.net.ConnectException;

@Component
public class ExternalServiceClient {

    private final RestTemplate restTemplate;

    @Value("${rest.url.basepath.client}")
    private String basePath;

    public ExternalServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @CircuitBreaker(name = "clientService", fallbackMethod = "fallbackClient")
    @Retry(name = "clientService", fallbackMethod = "fallbackClient")
    public ClientDTO getClient(String cpf) {
        System.out.println("Tentando chamar o serviço externo...");
        String url = basePath + cpf;
        return restTemplate.getForObject(url, ClientDTO.class);
    }

    public ClientDTO fallbackClient(String cpf, Throwable throwable) {
        //TODO ajustar o fallback
        System.out.println("Fallback Method for CPF: " + cpf);
        System.out.println("Error: " + throwable.getMessage());
        System.out.println("Class Name: " + throwable.getClass().getName());

        return new ClientDTO(); // Retorne um objeto válido ou um Optional vazio, conforme necessidade.
    }
}