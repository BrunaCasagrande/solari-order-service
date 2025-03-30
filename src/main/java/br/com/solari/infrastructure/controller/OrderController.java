package br.com.solari.infrastructure.controller;

import br.com.solari.application.domain.Order;
import br.com.solari.application.usecase.SearchOrderUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/solari/v1/order")
@RequiredArgsConstructor
public class OrderController {

    private final SearchOrderUseCase searchOrderUseCase;

    @GetMapping("/{id}")
    public Order getOrder(@PathVariable(name = "id") String id) {
        //ajustar retorno com DTO e avaliar arq num geral
        return this.searchOrderUseCase.getOrder(id);
    }

}
