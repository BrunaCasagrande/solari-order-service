package br.com.solari.infrastructure.gateway;

import br.com.solari.application.dto.InventoryDTO;
import br.com.solari.application.dto.ProductDTO;
import br.com.solari.application.gateway.OrchestrationGateway;
import br.com.solari.application.dto.ClientDTO;
import br.com.solari.infrastructure.rest.ExternalServiceClient;
import br.com.solari.infrastructure.rest.ExternalServiceInventory;
import br.com.solari.infrastructure.rest.ExternalServiceProduct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OrchestrationGatewayImpl implements OrchestrationGateway {

    private final ExternalServiceClient externalServiceClient;
    private final ExternalServiceProduct externalServiceProduct;
    private final ExternalServiceInventory externalServiceInventory;

    public Optional<ClientDTO> getClientFromClientService(String cpf){
        return Optional.ofNullable(externalServiceClient.getClient(cpf));
    }
    //criar para produto e criar um ExternalServiceProduct
    public Optional<ProductDTO> getProductFromProductService(String sku){
        return Optional.ofNullable(externalServiceProduct.getProduct(sku));
    }

    @Override
    public Optional<List<InventoryDTO>> getInventoryFromInventoryService(String sku) {
        List<InventoryDTO> inventoryList = externalServiceInventory.getInventory(sku);
        return inventoryList.isEmpty() ? Optional.empty() : Optional.of(inventoryList);
    }

    @Override
    public void updateInventoryInInventoryService(String sku, InventoryDTO inventoryDTO) {
        externalServiceInventory.updateInventory(sku, inventoryDTO);
    }
}
