package com.example.currencyrateprovider;

import com.example.rates.CurrencyRateServiceGrpc;
import com.example.rates.RateRequest;
import com.example.rates.RateResponse;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import java.time.Instant;
import java.util.Random;

@GrpcService
public class CurrencyRateServiceImpl extends CurrencyRateServiceGrpc.CurrencyRateServiceImplBase {
  private final Random random = new Random();

  @Override
  public void getRate(RateRequest request, StreamObserver<RateResponse> responseObserver) {
    String pair = request.getPair().isBlank() ? "USDRUB" : request.getPair();
    double value = 70.0 + (random.nextDouble() * 40.0); // 70..110

    RateResponse response = RateResponse.newBuilder()
        .setPair(pair)
        .setValue(value)
        .setTimestamp(Instant.now().toString())
        .build();

    responseObserver.onNext(response);
    responseObserver.onCompleted();
  }
}
