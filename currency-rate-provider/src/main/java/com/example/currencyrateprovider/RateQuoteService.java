package com.example.currencyrateprovider;

import com.example.rates.RateResponse;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Random;

@Service
public class RateQuoteService {
  private final Random random = new Random();

  public RateResponse buildRateResponse(String requestedPair) {
    String pair = requestedPair == null || requestedPair.isBlank() ? "USDRUB" : requestedPair;
    double value = 70.0 + (random.nextDouble() * 40.0);

    return RateResponse.newBuilder()
        .setPair(pair)
        .setValue(value)
        .setTimestamp(Instant.now().toString())
        .build();
  }
}
