package com.example.currencyrateprovider;

import com.example.rates.CurrencyRateServiceGrpc;
import com.example.rates.RateRequest;
import com.example.rates.RateResponse;
import io.grpc.stub.StreamObserver;
import java.util.Map;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class CurrencyRateServiceImpl extends CurrencyRateServiceGrpc.CurrencyRateServiceImplBase {

  private final RateQuoteService rateQuoteService;
  private final EventStreamLogger eventLogger;

  public CurrencyRateServiceImpl(RateQuoteService rateQuoteService, EventStreamLogger eventLogger) {
    this.rateQuoteService = rateQuoteService;
    this.eventLogger = eventLogger;
  }

  @Override
  public void getRate(RateRequest request, StreamObserver<RateResponse> responseObserver) {
    eventLogger.event("grpc.server.request_received", Map.of("pair", request.getPair()));
    try {
      RateResponse response = rateQuoteService.buildRateResponse(request.getPair());
      eventLogger.event(
          "grpc.server.response_sent",
          Map.of(
              "pair", response.getPair(),
              "value", response.getValue(),
              "timestamp", response.getTimestamp()));
      responseObserver.onNext(response);
      responseObserver.onCompleted();
    } catch (RuntimeException e) {
      eventLogger.event(
          "grpc.server.error",
          Map.of(
              "pair", request.getPair(),
              "error", e.getClass().getSimpleName(),
              "message", String.valueOf(e.getMessage())));
      throw e;
    }
  }
}
