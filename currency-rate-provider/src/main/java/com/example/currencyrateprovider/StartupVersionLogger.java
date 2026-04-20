package com.example.currencyrateprovider;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class StartupVersionLogger {

  @Autowired(required = false)
  private BuildProperties buildProperties;

  private final EventStreamLogger eventLogger;

  public StartupVersionLogger(EventStreamLogger eventLogger) {
    this.eventLogger = eventLogger;
  }

  @EventListener(ApplicationReadyEvent.class)
  public void logVersion() {
    String version = buildProperties != null ? buildProperties.getVersion() : "unknown";
    eventLogger.event("app.started", Map.of("version", version));
  }
}
