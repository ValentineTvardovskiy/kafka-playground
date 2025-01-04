package com.vatva.simplecommunicationapp.listener;

import com.vatva.simplecommunicationapp.dto.WeatherData;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ComplexKafkaListener implements MessageListener<String, WeatherData> {

  @Override
  public void onMessage(ConsumerRecord<String, WeatherData> message) {
    System.out.println(LocalDateTime.now() + " simple listener received: " + message.value());
  }
}
