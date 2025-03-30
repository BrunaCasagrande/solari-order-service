package br.com.solari.infrastructure.event.messaging;

import br.com.solari.application.usecase.ProcessOrderUseCase;
import br.com.solari.infrastructure.event.OrderEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OrderConsumer {
  private static final Logger logger = LoggerFactory.getLogger(OrderConsumer.class);
  private final ProcessOrderUseCase processOrderUseCase;
  private final ObjectMapper objectMapper = new ObjectMapper();

  public OrderConsumer(ProcessOrderUseCase processOrderUseCase) {
    this.processOrderUseCase = processOrderUseCase;
  }

  @KafkaListener(topics = "novo-pedido-queue", groupId = "my-group")
  public void consumirPedido(ConsumerRecord<String, String> record) {
    try {
      String json = record.value();
      OrderEvent orderEvent = objectMapper.readValue(json, OrderEvent.class);
      logger.info("Mensagem recebida do Kafka: {}", json);
      logger.info("Mensagem recebida e mapeada: {}",orderEvent.toString());

      processOrderUseCase.processOrder(orderEvent);
    } catch (Exception e) {
      logger.error("Erro ao processar mensagem do Kafka", e);
    }
  }
}
