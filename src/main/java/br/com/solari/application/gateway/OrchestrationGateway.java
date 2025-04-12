package br.com.solari.application.gateway;

import br.com.solari.application.dto.ClientDTO;
import br.com.solari.application.dto.ProductDTO;

import java.util.Optional;

public interface OrchestrationGateway {
    Optional<ClientDTO> getClientFromClientService(String cpf);
    Optional<ProductDTO> getProductFromProductService(String sku);
}
