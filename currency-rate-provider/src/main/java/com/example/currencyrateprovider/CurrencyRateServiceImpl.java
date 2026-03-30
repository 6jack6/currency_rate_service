package com.example.currencyrateprovider;

import com.example.rates.CurrencyRateServiceGrpc;
import com.example.rates.RateRequest;
import com.example.rates.RateResponse;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@GrpcService
public class CurrencyRateServiceImpl extends CurrencyRateServiceGrpc.CurrencyRateServiceImplBase {
  private static final Logger log = LoggerFactory.getLogger(CurrencyRateServiceImpl.class);

  private final RateQuoteService rateQuoteService;

  public CurrencyRateServiceImpl(RateQuoteService rateQuoteService) {
    this.rateQuoteService = rateQuoteService;
  }

  @Override
  public void getRate(RateRequest request, StreamObserver<RateResponse> responseObserver) {
    log.info("Server request: getRate pair={}", request.getPair());
    try {
      RateResponse response = rateQuoteService.buildRateResponse(request.getPair());
      log.info(
          "Server response: pair={} value={} timestamp={}",
          response.getPair(),
          response.getValue(),
          response.getTimestamp());
      responseObserver.onNext(response);
      responseObserver.onCompleted();
    } catch (RuntimeException e) {
      log.error("Server error building response for pair={}", request.getPair(), e);
      throw e;
    }
  }
}
