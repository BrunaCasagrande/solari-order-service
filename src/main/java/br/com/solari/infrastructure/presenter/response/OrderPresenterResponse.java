package br.com.solari.infrastructure.presenter.response;

import java.util.Map;
import lombok.Builder;

@Builder
public record OrderPresenterResponse(
    String id, String status, Map<String, Integer> products, String cpf) {}
