package com.vatva.debeziumembedded.controller;

import com.vatva.debeziumembedded.domain.Click;
import com.vatva.debeziumembedded.repository.ClickRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@Slf4j
@RestController
public class ClickController {

  private final ClickRepository clickRepository;

  public ClickController(ClickRepository clickRepository) {
    this.clickRepository = clickRepository;
  }

  @GetMapping("/click")
  public void handleClick() {
    Click click = new Click();
    click.setAt(Instant.now());
    clickRepository.save(click);
  }
}
