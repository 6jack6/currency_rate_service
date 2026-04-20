package com.example.currencyrateprovider;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class EventStreamLogger {
  private final ObjectMapper objectMapper;
  private final String serviceName;

  public EventStreamLogger(
      ObjectMapper objectMapper, @Value("${spring.application.name}") String serviceName) {
    this.objectMapper = objectMapper;
    this.serviceName = serviceName;
  }

  public void event(String eventName, Map<String, ?> payload) {
    Map<String, Object> record = new LinkedHashMap<>();
    record.put("ts", Instant.now().toString());
    record.put("service", serviceName);
    record.put("event", eventName);
    if (payload != null && !payload.isEmpty()) {
      record.put("data", payload);
    }
    write(record);
  }

  private void write(Map<String, Object> record) {
    try {
      System.out.println(objectMapper.writeValueAsString(record));
    } catch (JsonProcessingException e) {
      System.out.println(
          "{\"event\":\"event_stream_serialization_failed\",\"error\":\""
              + e.getMessage()
              + "\"}");
    }
  }
}
