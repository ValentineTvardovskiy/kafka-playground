package com.vatva;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Scanner;

import static java.lang.System.out;

public class BasicProducer {

  private final int ID;

  public BasicProducer(int id) {
    ID = id;
  }

  private KafkaProducer<String, String> producer;

  public void start() {
    final Map<String, Object> configs = Map.of(
        ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092",
        ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class,
        ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class,
        ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true
    );

    try {
      producer = new KafkaProducer<>(configs);
    } catch (Exception e) {
      out.println("Exception occurred");
    }
  }

  public void send(String topic, String key, String value) {
    System.out.println(ID + " Send message: " + value + " at: " + System.currentTimeMillis());
    Callback callback = (metadata, exception) ->
        out.format("Published with metadata: %s, error: %s%n", metadata, exception);
    producer.send(new ProducerRecord<>(topic, key, value), callback);
    System.out.println("Message sent: " + value + " at: " + System.currentTimeMillis());
  }
}
