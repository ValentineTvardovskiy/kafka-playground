package com.vatva.simplecommunicationapp.controller;

import com.vatva.simplecommunicationapp.service.WeatherService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class WeatherController {

  private final WeatherService weatherService;

  public WeatherController(WeatherService weatherService) {
    this.weatherService = weatherService;
  }

  @GetMapping("/weather")
  public ResponseEntity<Void> pullWeatherData(@RequestParam String city) {
    weatherService.pullWeather(city);
    return ResponseEntity.ok().build();
  }
}
