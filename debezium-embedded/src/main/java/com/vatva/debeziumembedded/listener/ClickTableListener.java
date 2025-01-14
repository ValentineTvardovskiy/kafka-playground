package com.vatva.debeziumembedded.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vatva.debeziumembedded.domain.Click;
import io.debezium.config.Configuration;
import io.debezium.engine.ChangeEvent;
import io.debezium.engine.DebeziumEngine;
import io.debezium.engine.format.Json;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Slf4j
@Component
public class ClickTableListener {

  private final Executor executor;
  private final ObjectMapper objectMapper;
  private final DebeziumEngine<ChangeEvent<String, String>> debeziumEngine;

  public ClickTableListener(ObjectMapper objectMapper, Configuration connector) {
    this.objectMapper = objectMapper;
    this.executor = Executors.newSingleThreadExecutor();
    this.debeziumEngine = DebeziumEngine.create(Json.class)
    // DebeziumEngine.create(KeyValueHeaderChangeEventFormat.of(Json.class, Json.class, Json.class), "io.debezium.embedded.async.ConvertingAsyncEngineBuilderFactory")
    // to use AsyncEmbeddedEngine - the records could be sent to the sink in a different order than the changes were done in the source database
    // once the AsyncEmbeddedEngine is closed, it cannot be started again and has to be re-created
        .using(connector.asProperties())
        .notifying(this::handleEvent)
        .build();
  }

  private void handleEvent(ChangeEvent<String, String> event) {
    try {
      System.out.println("Key: " + event.key());
      System.out.println("Value: " + event.value());
      var changeEventPayload = objectMapper.readValue(event.value(), ChangeEventValue.class).payload();
      if (changeEventPayload.source().table().equals("click")) {
        var clickBefore = !changeEventPayload.before().toString().equals("null")
            ? objectMapper.readValue(changeEventPayload.before().toString(), Click.class) : null;
        var clickAfter = !changeEventPayload.after().toString().equals("null")
            ? objectMapper.readValue(changeEventPayload.after().toString(), Click.class) : null;
        System.out.println("Old value: " + clickBefore);
        System.out.println("New value: " + clickAfter);
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }

  @PostConstruct
  private void start() {
    this.executor.execute(debeziumEngine);
  }

  @PreDestroy
  private void stop() throws IOException {
    if (this.debeziumEngine != null) {
      this.debeziumEngine.close();
    }
  }
}
