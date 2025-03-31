package br.com.solari.infrastructure.presenter.response;

import lombok.Builder;

import java.util.Map;

@Builder
public record OrderPresenterResponse(String id,
                                     String status,
                                     Map<Integer, Integer> products,
                                     Integer clientId) {}
