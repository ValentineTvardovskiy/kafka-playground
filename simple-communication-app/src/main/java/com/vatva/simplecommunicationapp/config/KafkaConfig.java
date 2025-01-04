package com.vatva.simplecommunicationapp.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vatva.simplecommunicationapp.dto.WeatherData;
import com.vatva.simplecommunicationapp.listener.ComplexKafkaListener;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;

@Configuration
public class KafkaConfig {

  @Value("${app.topic}")
  private String weatherTopic;

  @Value("${spring.kafka.bootstrap-servers}")
  private String broker;

  @Bean
  public ConcurrentMessageListenerContainer<String, WeatherData> kafkaListenerContainer(
      ConsumerFactory<String, WeatherData> consumerFactory,
      ComplexKafkaListener complexKafkaListener) {

    ContainerProperties containerProperties = new ContainerProperties("weather-data");
    containerProperties.setMessageListener(complexKafkaListener);

    ConcurrentMessageListenerContainer<String, WeatherData> container =
        new ConcurrentMessageListenerContainer<>(consumerFactory, containerProperties);

    container.setConcurrency(1);
    return container;
  }

  @Bean
  public ConsumerFactory<String, WeatherData> consumerFactory(ObjectMapper objectMapper) {
    var keyDeserializer = new StringDeserializer();
    var valueDeserializer = new JsonDeserializer<WeatherData>(objectMapper);
    valueDeserializer.addTrustedPackages("*");
    var configs = new HashMap<String, Object>();
    configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, broker);
    configs.put(ConsumerConfig.GROUP_ID_CONFIG, "complex-listener-group");
    return new DefaultKafkaConsumerFactory<>(configs, keyDeserializer, valueDeserializer);
  }

  @Bean
  public KafkaTemplate<String, WeatherData> kafkaTemplate(ProducerFactory<String, WeatherData> producerFactory) {
    var template = new KafkaTemplate<>(producerFactory);
    template.setDefaultTopic(weatherTopic);
    return template;
  }

  @Bean
  public ProducerFactory<String, WeatherData> producerFactory(ObjectMapper objectMapper) {
    var keySerializer = new StringSerializer();
    var valueSerializer = new JsonSerializer<WeatherData>(objectMapper);
    var configs = new HashMap<String, Object>();
    configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, broker);
    return new DefaultKafkaProducerFactory<>(configs, keySerializer, valueSerializer);
  }
}
