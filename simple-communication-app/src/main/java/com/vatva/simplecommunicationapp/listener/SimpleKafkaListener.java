package com.vatva.simplecommunicationapp.listener;

import com.vatva.simplecommunicationapp.dto.WeatherData;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
//@KafkaListener(topics = "${app.topic}", groupId = "simple-listener-group", containerFactory = "simpleListenerFactory")
public class SimpleKafkaListener {

  //@KafkaHandler(isDefault = true)
  public void processMessage(@Payload WeatherData message) {
    System.out.println(LocalDateTime.now() + " simple listener received: " + message);
  }
}
