package com.vatva.simplecommunicationapp.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record WindData(@JsonProperty("speed") Integer speed,
                       @JsonProperty("deg") Integer deg,
                       @JsonProperty("gust") Integer gust) {
}
