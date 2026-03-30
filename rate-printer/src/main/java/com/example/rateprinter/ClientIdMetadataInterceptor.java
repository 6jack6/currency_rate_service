package com.example.rateprinter;

import io.grpc.CallOptions;
import io.grpc.Channel;
import io.grpc.ClientCall;
import io.grpc.ClientInterceptor;
import io.grpc.ForwardingClientCall;
import io.grpc.Metadata;
import io.grpc.MethodDescriptor;
import net.devh.boot.grpc.client.interceptor.GrpcGlobalClientInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@GrpcGlobalClientInterceptor
public class ClientIdMetadataInterceptor implements ClientInterceptor {

  private static final Metadata.Key<String> CLIENT_ID_KEY =
      Metadata.Key.of("client-id", Metadata.ASCII_STRING_MARSHALLER);

  private final String clientId;

  public ClientIdMetadataInterceptor(@Value("${app.client-id}") String clientId) {
    this.clientId = clientId;
  }

  @Override
  public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(
      MethodDescriptor<ReqT, RespT> method,
      CallOptions callOptions,
      Channel next) {
    return new ForwardingClientCall.SimpleForwardingClientCall<ReqT, RespT>(
        next.newCall(method, callOptions)) {
      @Override
      public void start(Listener<RespT> responseListener, Metadata headers) {
        headers.discardAll(CLIENT_ID_KEY);
        headers.put(CLIENT_ID_KEY, clientId);
        super.start(responseListener, headers);
      }
    };
  }
}
