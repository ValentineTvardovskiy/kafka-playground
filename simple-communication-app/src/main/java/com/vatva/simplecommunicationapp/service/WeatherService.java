package com.vatva.simplecommunicationapp.service;

import com.vatva.simplecommunicationapp.dto.WeatherData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Service
public class WeatherService {

  @Value("${app.topic}")
  private String weatherTopic;

  private final WebClient webClient;
  private final KafkaTemplate<String, WeatherData> kafkaTemplate;

  public WeatherService(WebClient webClient, KafkaTemplate<String, WeatherData> kafkaTemplate) {
    this.webClient = webClient;
    this.kafkaTemplate = kafkaTemplate;
  }

  public void pullWeather(String city) {
    var query = "q=" + city;
    var fullUrl = "https://api.openweathermap.org/data/2.5/weather?" + query + "&units=metric&appid=3f0b2075a4ed0c3410ea5cb0b9d05d00";
    webClient.get().uri(fullUrl)
        .retrieve()
        .bodyToMono(WeatherData.class)
        .doOnError(ex -> log.info("Error on weather get: {}", ex.getMessage()))
        .doOnSuccess(this::processWeatherResponse)
        .subscribe();
  }

  public void processWeatherResponse(WeatherData response) {
    kafkaTemplate.send(weatherTopic, response.city(), response)
        .whenComplete((result, ex) -> {
          if (ex != null) {
            log.error("Weather data sent to kafka failed: {}", ex.getMessage());
          } else {
            log.info("Weather data sent to kafka");
          }
        });
  }
}
