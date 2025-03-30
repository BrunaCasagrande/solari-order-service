package br.com.solari.infrastructure.controller;

import br.com.solari.application.usecase.SearchOrderUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/solari/v1")
@RequiredArgsConstructor
public class OrderController {

    private final SearchOrderUseCase searchOrderUseCase;
/*
    @GetMapping("/{id}")
    public ResponseEntity<ClientPresenterResponse> findByCpf(@PathVariable final String cpf) {
        return this.searchOrderUseCase
                .execute(cpf)
                .map(client -> ResponseEntity.ok(clientPresenter.parseToResponse(client)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
*/
}
