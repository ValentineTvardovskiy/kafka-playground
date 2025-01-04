package com.vatva.simplecommunicationapp.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record BasicData(@JsonProperty("temp") int temp,
                        @JsonProperty("feels_like") int feelsLike,
                        @JsonProperty("humidity") int humidity) {
}
