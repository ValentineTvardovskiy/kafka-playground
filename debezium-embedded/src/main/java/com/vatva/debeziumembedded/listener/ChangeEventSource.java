package com.vatva.debeziumembedded.listener;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ChangeEventSource(String table) {
}
