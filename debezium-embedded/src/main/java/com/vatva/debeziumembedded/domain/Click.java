package com.vatva.debeziumembedded.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;

@ToString
@Getter
@Setter
@Entity
public class Click {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;
  private Instant at;
}
