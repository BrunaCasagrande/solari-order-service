package br.com.solari.infrastructure.config;

import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class SolariOrderServiceConfig {

  @Value("${spring.kafka.bootstrap-servers}")
  private String server_config;

  @Bean
  public KafkaTemplate<String, String> kafkaTemplate() {

    Map<String, Object> producerProps = new HashMap<>();
    producerProps.put(
        ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, server_config);
    producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

    ProducerFactory<String, String> producerFactory =
        new DefaultKafkaProducerFactory<>(producerProps);

    return new KafkaTemplate<>(producerFactory);
  }

  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }

}
