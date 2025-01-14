package com.vatva.debeziumembedded.listener;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ChangeEventPayload(JsonNode before, JsonNode after, ChangeEventSource source, String op) {
}