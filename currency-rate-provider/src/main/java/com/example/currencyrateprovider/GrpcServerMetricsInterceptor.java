package com.example.currencyrateprovider;

import io.grpc.ForwardingServerCall;
import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import io.grpc.Status;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import net.devh.boot.grpc.server.interceptor.GrpcGlobalServerInterceptor;
import org.springframework.stereotype.Component;

@Component
@GrpcGlobalServerInterceptor
public class GrpcServerMetricsInterceptor implements ServerInterceptor {

  static final Metadata.Key<String> CLIENT_ID_KEY =
      Metadata.Key.of("client-id", Metadata.ASCII_STRING_MARSHALLER);

  private final MeterRegistry registry;

  public GrpcServerMetricsInterceptor(MeterRegistry registry) {
    this.registry = registry;
  }

  @Override
  public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
      ServerCall<ReqT, RespT> call,
      Metadata headers,
      ServerCallHandler<ReqT, RespT> next) {
    String clientId = Optional.ofNullable(headers.get(CLIENT_ID_KEY)).orElse("unknown");
    long startNanos = System.nanoTime();

    ServerCall<ReqT, RespT> wrapped =
        new ForwardingServerCall.SimpleForwardingServerCall<ReqT, RespT>(call) {
          @Override
          public void close(Status status, Metadata trailers) {
            long elapsed = System.nanoTime() - startNanos;
            Timer.builder("grpc.server.requests")
                .tag("client", clientId)
                .publishPercentiles(0.5, 0.95, 0.99)
                .publishPercentileHistogram()
                .register(registry)
                .record(elapsed, TimeUnit.NANOSECONDS);

            if (!status.isOk()) {
              Counter.builder("grpc.server.errors")
                  .tag("client", clientId)
                  .tag("code", status.getCode().name())
                  .register(registry)
                  .increment();
            }
            super.close(status, trailers);
          }
        };
    return next.startCall(wrapped, headers);
  }
}
