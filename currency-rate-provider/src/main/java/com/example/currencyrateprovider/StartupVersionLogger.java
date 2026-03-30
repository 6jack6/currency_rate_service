package com.example.currencyrateprovider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class StartupVersionLogger {
  private static final Logger log = LoggerFactory.getLogger(StartupVersionLogger.class);

  @Autowired(required = false)
  private BuildProperties buildProperties;

  @Value("${spring.application.name}")
  private String applicationName;

  @EventListener(ApplicationReadyEvent.class)
  public void logVersion() {
    String version =
        buildProperties != null ? buildProperties.getVersion() : "unknown";
    log.info("Application {} started, version {}", applicationName, version);
  }
}
