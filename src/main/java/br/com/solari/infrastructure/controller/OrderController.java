package br.com.solari.infrastructure.controller;

import br.com.solari.application.usecase.SearchOrderUseCase;
import br.com.solari.infrastructure.presenter.OrderPresenter;
import br.com.solari.infrastructure.presenter.response.OrderPresenterResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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
    private final OrderPresenter orderPresenter;

    @GetMapping("/{id}")
    public ResponseEntity<OrderPresenterResponse> getOrder(@PathVariable(name = "id") String id) {
        log.info("Obtendo dados do pedido id: {}", id);
        return this.searchOrderUseCase
                .getOrder(id)
                .map(order -> ResponseEntity.ok(orderPresenter.parseToResponse(order)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

}
