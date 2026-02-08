package com.example.rateprinter;

import com.example.rates.CurrencyRateServiceGrpc;
import com.example.rates.RateRequest;
import com.example.rates.RateResponse;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class RatePrinterService {
  @GrpcClient("currency-rate-provider")
  private CurrencyRateServiceGrpc.CurrencyRateServiceBlockingStub blockingStub;

  @Scheduled(fixedDelay = 5000)
  public void printRate() {
    RateRequest request = RateRequest.newBuilder().setPair("USDRUB").build();
    RateResponse response = blockingStub.getRate(request);

    System.out.printf("%s %s = %.4f%n", response.getTimestamp(), response.getPair(), response.getValue());
  }
}
