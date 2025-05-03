package br.com.solari.application.gateway;

import br.com.solari.application.dto.ClientDTO;
import br.com.solari.application.dto.InventoryDTO;
import br.com.solari.application.dto.ProductDTO;

import java.util.List;
import java.util.Optional;

public interface OrchestrationGateway {
    Optional<ClientDTO> getClientFromClientService(String cpf);
    Optional<ProductDTO> getProductFromProductService(String sku);
    Optional<List<InventoryDTO>> getInventoryFromInventoryService(String sku);
    void updateInventoryInInventoryService(String sku, InventoryDTO inventoryDTO);
}
