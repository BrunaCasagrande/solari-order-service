package br.com.solari.infrastructure.gateway;

import br.com.solari.application.dto.ProductDTO;
import br.com.solari.application.gateway.OrchestrationGateway;
import br.com.solari.application.dto.ClientDTO;
import br.com.solari.infrastructure.rest.ExternalServiceClient;
import br.com.solari.infrastructure.rest.ExternalServiceProduct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OrchestrationGatewayImpl implements OrchestrationGateway {

    private final ExternalServiceClient externalServiceClient;
    private final ExternalServiceProduct externalServiceProduct;

    public Optional<ClientDTO> getClientFromClientService(String cpf){
        return Optional.ofNullable(externalServiceClient.getClient(cpf));
    }
    //criar para produto e criar um ExternalServiceProduct
    public Optional<ProductDTO> getProductFromProductService(String sku){
        return Optional.ofNullable(externalServiceProduct.getProduct(sku));
    }
}
