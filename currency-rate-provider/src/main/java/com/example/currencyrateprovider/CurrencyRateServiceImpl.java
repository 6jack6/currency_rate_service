package com.example.currencyrateprovider;

import com.example.rates.CurrencyRateServiceGrpc;
import com.example.rates.RateRequest;
import com.example.rates.RateResponse;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class CurrencyRateServiceImpl extends CurrencyRateServiceGrpc.CurrencyRateServiceImplBase {
  private final RateQuoteService rateQuoteService;

  public CurrencyRateServiceImpl(RateQuoteService rateQuoteService) {
    this.rateQuoteService = rateQuoteService;
  }

  @Override
  public void getRate(RateRequest request, StreamObserver<RateResponse> responseObserver) {
    RateResponse response = rateQuoteService.buildRateResponse(request.getPair());

    responseObserver.onNext(response);
    responseObserver.onCompleted();
  }
}
