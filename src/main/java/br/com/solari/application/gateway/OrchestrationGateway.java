package br.com.solari.application.gateway;

import br.com.solari.infrastructure.presenter.dto.ClientDTO;

import java.util.Optional;

public interface OrchestrationGateway {
    Optional<ClientDTO> getClientFromClientService(String cpf);
}
