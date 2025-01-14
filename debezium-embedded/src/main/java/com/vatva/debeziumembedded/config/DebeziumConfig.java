package com.vatva.debeziumembedded.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DebeziumConfig {

  @Value("${spring.datasource.username}")
  private String username;

  @Value("${spring.datasource.username}")
  private String password;

  @Bean
  public io.debezium.config.Configuration postgresDebeziumConfig() {
    return io.debezium.config.Configuration.create()
        .with("name", "embedded-psql-connector")
        .with("connector.class", "io.debezium.connector.postgresql.PostgresConnector")
        .with("offset.storage", "org.apache.kafka.connect.storage.FileOffsetBackingStore")
        .with("offset.storage.file.filename", "/tmp/offsets.dat")
        .with("offset.flush.interval.ms", "60000")
        .with("database.hostname", "127.0.0.1")
        .with("database.port", "5432")
        .with("database.user", username)
        .with("database.password", password)
        .with("database.dbname", "debeziumtest")
        .with("database.server.name", "embedded-postgres-db-server")
        .with("include.schema.changes", "false")
        .with("plugin.name", "pgoutput")
        .with("topic.prefix", "cdc")
        .with("table.include.list", "public.click")
        .build();
  }
}
