package br.com.solari.infrastructure.gateway;

import br.com.solari.application.gateway.OrchestrationGateway;
import br.com.solari.infrastructure.presenter.dto.ClientDTO;
import br.com.solari.infrastructure.rest.ExternalServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OrchestrationGatewayImpl implements OrchestrationGateway {

    private final ExternalServiceClient externalServiceClient;

    public Optional<ClientDTO> getClientFromClientService(String cpf){
        return Optional.ofNullable(externalServiceClient.getClient(cpf));
    }
}
