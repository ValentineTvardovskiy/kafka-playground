package com.vatva.simplecommunicationapp.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record WeatherData(@JsonProperty("main") BasicData main,
                          @JsonProperty("name") String city,
                          @JsonProperty("wind") WindData wind,
                          @JsonProperty("visibility") Integer visibility) {
}
