package com.vatva;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;

import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    var topic = "basic-kafka-topic";
    createTopic(topic);

    var producersMap = new HashMap<Integer, BasicProducer>();
    var consumerThreadsMap = new HashMap<Integer, Thread>();

    try (var scanner = new Scanner(System.in)) {
      System.out.println("Write a message:");
      while (true) {
        String input = scanner.nextLine();

        if ("exit".equalsIgnoreCase(input)) {
          break;
        }

        if (input.isBlank()) {
          continue;
        }

        var start = input.substring(0, 1);
        if (start.equals(" ")) {
          producersMap.forEach((k, v) -> v.send(topic, String.valueOf(k), input));
          continue;
        }

        int id = Integer.parseInt(start);
        if (input.contains("+p")) {
          producersMap.put(id, createProducer(id));
        } else if (input.contains("-p")) {
          producersMap.remove(id);
          System.out.println("Producers size" + producersMap.size());
        } else if (input.contains("+c")) {
          consumerThreadsMap.put(id, createConsumer(id));
        } else if (input.contains("-c")) {
          consumerThreadsMap.remove(id).interrupt();
          System.out.println("Consumers size" + consumerThreadsMap.size());
        } else {
          producersMap.get(id).send(topic, String.valueOf(id), input);
        }
      }
    }
  }

  private static void createTopic(String topic) {
    final var topicConfigs = new HashMap<String, Object>();
    topicConfigs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");

    try (var adminClient = AdminClient.create(topicConfigs)) {
      short partitions = 3;
      short replicationFactor = 1;
      NewTopic newTopic = new NewTopic(topic, partitions, replicationFactor);
      adminClient.createTopics(List.of(newTopic)).all().whenComplete((r, e) -> {
        if (e != null) {
          System.out.println("Topic creation failed: " + e.getMessage());
        } else {
          System.out.println("Topic created");
        }
      });
    }
  }

  public static BasicProducer createProducer(int id) {
    var producer = new BasicProducer(id);
    producer.start();
    return producer;
  }

  public static Thread createConsumer(int id) {
    var consumer = new BasicConsumer(id);
    var consumerThread = new Thread(consumer::start);

    consumerThread.start();
    System.out.println(id + " - Consumer started in a separate thread.");
    return consumerThread;
  }
}