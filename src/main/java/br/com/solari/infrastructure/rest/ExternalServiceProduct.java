package br.com.solari.infrastructure.rest;

import br.com.solari.application.dto.InventoryDTO;
import br.com.solari.application.dto.ProductDTO;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class ExternalServiceProduct {

    private final RestTemplate restTemplate;

    @Value("${rest.url.basepath.product}")
    private String basePath;

    public ExternalServiceProduct(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Retry(name = "retryProductService", fallbackMethod = "fallbackProduct")
    @CircuitBreaker(name = "circuitBrakerProductService")
    public ProductDTO getProduct(String sku){
        log.info("Realizando chamada de  serviço externo: {}", basePath);
        String url = basePath + sku;
        return restTemplate.getForObject(url, ProductDTO.class);
    }

    public ProductDTO fallbackProduct(String sku, Throwable throwable) {
        log.info("Class Name Fallback Error: {}",  throwable.getClass().getName());
        throw new RuntimeException("Erro no fallback para SKU " + sku, throwable);
    }

}
