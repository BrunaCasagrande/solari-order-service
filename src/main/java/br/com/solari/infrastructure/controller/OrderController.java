package br.com.solari.infrastructure.controller;

import br.com.solari.application.domain.Order;
import br.com.solari.application.usecase.PlaceOrder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/solari/v1")
@RequiredArgsConstructor
public class OrderController {

  @Autowired private PlaceOrder placeOrder;

  @PostMapping("/order")
  public ResponseEntity<Order> sendPedido(@Valid @RequestBody final Order request) {

    Order orderPlaced = placeOrder.createOrderAndSendToKafka(request);

    return ResponseEntity.status(HttpStatus.CREATED).body(orderPlaced);

  }
}
