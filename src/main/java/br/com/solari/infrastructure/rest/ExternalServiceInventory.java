package br.com.solari.infrastructure.rest;

import br.com.solari.application.dto.InventoryDTO;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class ExternalServiceInventory {

  private final RestTemplate restTemplate;

  @Value("${rest.url.basepath.inventory}")
  private String basePath;

  public ExternalServiceInventory(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  @Retry(name = "retryInventoryService", fallbackMethod = "fallbackInventory")
  @CircuitBreaker(name = "circuitBrakerInventoryService")
  public List<InventoryDTO> getInventory(String sku) {
    log.info("Realizando chamada para o serviço externo: {}", basePath);
    String url = basePath + "/sku/" + sku;
    return Arrays.asList(restTemplate.getForObject(url, InventoryDTO[].class));
  }

  public List<InventoryDTO> fallbackInventory(String sku, Throwable throwable) {
    log.info("Class Name Fallback Error: {}", throwable.getClass().getName());
    throw new RuntimeException("Erro no fallback para SKU: " + sku, throwable);
  }

  public void updateInventory(String sku, InventoryDTO inventoryDTO) {
    String url = basePath + "/sku/" + sku;
    restTemplate.put(url, inventoryDTO);
  }
}
