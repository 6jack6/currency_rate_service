package com.example.rateprinter;

import com.example.rates.CurrencyRateServiceGrpc;
import com.example.rates.RateRequest;
import com.example.rates.RateResponse;
import io.grpc.StatusRuntimeException;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class RatePrinterService {
  private static final Logger log = LoggerFactory.getLogger(RatePrinterService.class);

  @GrpcClient("currency-rate-provider")
  private CurrencyRateServiceGrpc.CurrencyRateServiceBlockingStub blockingStub;

  @Scheduled(initialDelay = 5000, fixedDelay = 5000)
  public void printRate() {
    try {
      RateRequest request = RateRequest.newBuilder().setPair("USDRUB").build();
      RateResponse response = blockingStub.getRate(request);

      System.out.printf("%s %s = %.4f%n", response.getTimestamp(), response.getPair(), response.getValue());
    } catch (StatusRuntimeException e) {
      log.warn("Rate provider is not ready yet: {}", e.getStatus().getDescription());
    }
  }
}
