package br.com.solari.infrastructure.event.messaging;

import br.com.solari.application.usecase.ProcessOrderUseCase;
import br.com.solari.infrastructure.event.OrderEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderConsumer {

  private final ProcessOrderUseCase processOrderUseCase;
  private final ObjectMapper objectMapper = new ObjectMapper();

  @KafkaListener(topics = "${spring.kafka.consumer.topic}", groupId = "${spring.kafka.consumer.group-id}")
  public void consumirPedido(ConsumerRecord<String, String> record) {
    try {
      String json = record.value();
      OrderEvent orderEvent = objectMapper.readValue(json, OrderEvent.class);
      log.info("Mensagem recebida do Kafka: {}", json);
      log.info("Mensagem recebida e mapeada: {}",orderEvent.toString());

      processOrderUseCase.processOrder(orderEvent);
    } catch (Exception e) {
      log.error("Erro ao processar mensagem do Kafka", e);
    }
  }
}
