package com.vatva;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Map;
import java.util.Set;

import static java.time.temporal.ChronoUnit.SECONDS;

public class BasicConsumer {

  private final int ID;

  public BasicConsumer(int id) {
    ID = id;
  }

  public void start() {
    final var topic = "basic-kafka-topic";
    final Map<String, Object> configs = Map.of(
        ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092",
        ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName(),
        ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName(),
        ConsumerConfig.GROUP_ID_CONFIG, "initial-test-group",
        ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest",
        ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false
    );

    try (var consumer = new KafkaConsumer<>(configs)) {
      consumer.subscribe(Set.of(topic));
      System.out.println("Consumer " + ID + " is ready");
      while (true) {
        var records = consumer.poll(Duration.of(1, SECONDS));
        for (var record : records) {

          System.out.println("Consumer " + ID + " Received record: " + record + " at: " + System.currentTimeMillis());
        }
        consumer.commitAsync();
      }
    }
  }
}
