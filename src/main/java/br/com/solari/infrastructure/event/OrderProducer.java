package br.com.solari.infrastructure.event;

import br.com.solari.application.domain.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderProducer {
  @Autowired private KafkaTemplate<String, String> kafkaTemplate;

  public void sendMessage(Order order) {
    kafkaTemplate.send("novo-pedido-queue", order.toString());
  }
}
