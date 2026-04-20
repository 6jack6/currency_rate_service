package com.example.rateprinter;

import com.example.rates.CurrencyRateServiceGrpc;
import com.example.rates.RateRequest;
import com.example.rates.RateResponse;
import io.grpc.StatusRuntimeException;
import java.util.Map;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class RatePrinterService {

  @GrpcClient("currency-rate-provider")
  private CurrencyRateServiceGrpc.CurrencyRateServiceBlockingStub blockingStub;

  private final EventStreamLogger eventLogger;

  public RatePrinterService(EventStreamLogger eventLogger) {
    this.eventLogger = eventLogger;
  }

  @Scheduled(initialDelay = 5000, fixedDelay = 5000)
  public void printRate() {
    try {
      RateRequest request = RateRequest.newBuilder().setPair("USDRUB").build();
      eventLogger.event("grpc.client.request_sent", Map.of("pair", request.getPair()));
      RateResponse response = blockingStub.getRate(request);
      eventLogger.event(
          "grpc.client.response_received",
          Map.of(
              "pair", response.getPair(),
              "value", response.getValue(),
              "timestamp", response.getTimestamp()));
    } catch (StatusRuntimeException e) {
      eventLogger.event(
          "grpc.client.error",
          Map.of(
              "status", e.getStatus().getCode().name(),
              "description", String.valueOf(e.getStatus().getDescription())));
    }
  }
}
