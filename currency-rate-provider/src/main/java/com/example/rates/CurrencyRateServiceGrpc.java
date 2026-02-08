package com.example.rates;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.62.2)",
    comments = "Source: rates.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class CurrencyRateServiceGrpc {

  private CurrencyRateServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "rates.CurrencyRateService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.example.rates.RateRequest,
      com.example.rates.RateResponse> getGetRateMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetRate",
      requestType = com.example.rates.RateRequest.class,
      responseType = com.example.rates.RateResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.example.rates.RateRequest,
      com.example.rates.RateResponse> getGetRateMethod() {
    io.grpc.MethodDescriptor<com.example.rates.RateRequest, com.example.rates.RateResponse> getGetRateMethod;
    if ((getGetRateMethod = CurrencyRateServiceGrpc.getGetRateMethod) == null) {
      synchronized (CurrencyRateServiceGrpc.class) {
        if ((getGetRateMethod = CurrencyRateServiceGrpc.getGetRateMethod) == null) {
          CurrencyRateServiceGrpc.getGetRateMethod = getGetRateMethod =
              io.grpc.MethodDescriptor.<com.example.rates.RateRequest, com.example.rates.RateResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetRate"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.rates.RateRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.rates.RateResponse.getDefaultInstance()))
              .setSchemaDescriptor(new CurrencyRateServiceMethodDescriptorSupplier("GetRate"))
              .build();
        }
      }
    }
    return getGetRateMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static CurrencyRateServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<CurrencyRateServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<CurrencyRateServiceStub>() {
        @java.lang.Override
        public CurrencyRateServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new CurrencyRateServiceStub(channel, callOptions);
        }
      };
    return CurrencyRateServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static CurrencyRateServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<CurrencyRateServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<CurrencyRateServiceBlockingStub>() {
        @java.lang.Override
        public CurrencyRateServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new CurrencyRateServiceBlockingStub(channel, callOptions);
        }
      };
    return CurrencyRateServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static CurrencyRateServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<CurrencyRateServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<CurrencyRateServiceFutureStub>() {
        @java.lang.Override
        public CurrencyRateServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new CurrencyRateServiceFutureStub(channel, callOptions);
        }
      };
    return CurrencyRateServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default void getRate(com.example.rates.RateRequest request,
        io.grpc.stub.StreamObserver<com.example.rates.RateResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetRateMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service CurrencyRateService.
   */
  public static abstract class CurrencyRateServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return CurrencyRateServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service CurrencyRateService.
   */
  public static final class CurrencyRateServiceStub
      extends io.grpc.stub.AbstractAsyncStub<CurrencyRateServiceStub> {
    private CurrencyRateServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected CurrencyRateServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new CurrencyRateServiceStub(channel, callOptions);
    }

    /**
     */
    public void getRate(com.example.rates.RateRequest request,
        io.grpc.stub.StreamObserver<com.example.rates.RateResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetRateMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service CurrencyRateService.
   */
  public static final class CurrencyRateServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<CurrencyRateServiceBlockingStub> {
    private CurrencyRateServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected CurrencyRateServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new CurrencyRateServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.example.rates.RateResponse getRate(com.example.rates.RateRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetRateMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service CurrencyRateService.
   */
  public static final class CurrencyRateServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<CurrencyRateServiceFutureStub> {
    private CurrencyRateServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected CurrencyRateServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new CurrencyRateServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.example.rates.RateResponse> getRate(
        com.example.rates.RateRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetRateMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_GET_RATE = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final AsyncService serviceImpl;
    private final int methodId;

    MethodHandlers(AsyncService serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_GET_RATE:
          serviceImpl.getRate((com.example.rates.RateRequest) request,
              (io.grpc.stub.StreamObserver<com.example.rates.RateResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
    return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
        .addMethod(
          getGetRateMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.example.rates.RateRequest,
              com.example.rates.RateResponse>(
                service, METHODID_GET_RATE)))
        .build();
  }

  private static abstract class CurrencyRateServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    CurrencyRateServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.example.rates.RatesProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("CurrencyRateService");
    }
  }

  private static final class CurrencyRateServiceFileDescriptorSupplier
      extends CurrencyRateServiceBaseDescriptorSupplier {
    CurrencyRateServiceFileDescriptorSupplier() {}
  }

  private static final class CurrencyRateServiceMethodDescriptorSupplier
      extends CurrencyRateServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    CurrencyRateServiceMethodDescriptorSupplier(java.lang.String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (CurrencyRateServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new CurrencyRateServiceFileDescriptorSupplier())
              .addMethod(getGetRateMethod())
              .build();
        }
      }
    }
    return result;
  }
}
