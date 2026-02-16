package com.example.currencyrateprovider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cloud.client.serviceregistry.AbstractAutoServiceRegistration;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ServiceRegistryStartup {

  private final AbstractAutoServiceRegistration autoServiceRegistration;

  public ServiceRegistryStartup(
      @Autowired(required = false) AbstractAutoServiceRegistration autoServiceRegistration) {
    this.autoServiceRegistration = autoServiceRegistration;
  }

  @EventListener(ApplicationReadyEvent.class)
  public void registerServiceIfNeeded() {
    if (autoServiceRegistration != null && !autoServiceRegistration.isRunning()) {
      autoServiceRegistration.start();
    }
  }
}
