package br.com.solari.infrastructure.presenter.response;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Builder
public record OrderPresenterResponse(String id,
                                     String status,
                                     Map<String, Integer> products,
                                     String cpf) {}
