package com.example.currencyrateprovider;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cloud.client.serviceregistry.AbstractAutoServiceRegistration;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ServiceRegistryStartup {

  private final AbstractAutoServiceRegistration autoServiceRegistration;
  private final EventStreamLogger eventLogger;

  public ServiceRegistryStartup(
      @Autowired(required = false) AbstractAutoServiceRegistration autoServiceRegistration,
      EventStreamLogger eventLogger) {
    this.autoServiceRegistration = autoServiceRegistration;
    this.eventLogger = eventLogger;
  }

  @EventListener(ApplicationReadyEvent.class)
  public void registerServiceIfNeeded() {
    if (autoServiceRegistration == null) {
      eventLogger.event("service.registry.unavailable", Map.of());
      return;
    }

    if (!autoServiceRegistration.isRunning()) {
      autoServiceRegistration.start();
      eventLogger.event("service.registry.started", Map.of());
      return;
    }

    eventLogger.event("service.registry.already_running", Map.of());
  }
}
