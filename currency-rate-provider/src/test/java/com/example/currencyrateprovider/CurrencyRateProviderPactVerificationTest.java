package com.example.currencyrateprovider;

import au.com.dius.pact.provider.MessageAndMetadata;
import au.com.dius.pact.provider.PactVerifyProvider;
import au.com.dius.pact.provider.junitsupport.Provider;
import au.com.dius.pact.provider.junitsupport.State;
import au.com.dius.pact.provider.junitsupport.loader.PactBroker;
import au.com.dius.pact.provider.junit5.MessageTestTarget;
import au.com.dius.pact.provider.junit5.PactVerificationContext;
import au.com.dius.pact.provider.junit5.PactVerificationInvocationContextProvider;
import com.example.rates.RateResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@SpringBootTest(properties = {
    "spring.cloud.zookeeper.enabled=false",
    "spring.cloud.zookeeper.discovery.enabled=false",
    "spring.cloud.zookeeper.discovery.register=false",
    "spring.cloud.service-registry.auto-registration.enabled=false",
    "grpc.server.port=-1"
})
@ActiveProfiles("test")
@Provider("currency-rate-provider")
@PactBroker(url = "${pactbroker.url:http://localhost:9292}")
class CurrencyRateProviderPactVerificationTest {
  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  @Autowired
  private RateQuoteService rateQuoteService;

  private String pair;

  @BeforeEach
  void before(PactVerificationContext context) {
    pair = "USDRUB";
    context.setTarget(new MessageTestTarget());
  }

  @TestTemplate
  @ExtendWith(PactVerificationInvocationContextProvider.class)
  void verifiesPactsFromBroker(PactVerificationContext context) {
    context.verifyInteraction();
  }

  @State("rate for pair exists")
  void rateForPairExists(Map<String, Object> params) {
    Object requestedPair = params == null ? null : params.get("pair");
    pair = requestedPair == null ? "USDRUB" : requestedPair.toString();
  }

  @PactVerifyProvider("get rate for requested currency pair")
  MessageAndMetadata verifyGetRate() throws JsonProcessingException {
    RateResponse response = rateQuoteService.buildRateResponse(pair);
    byte[] body = OBJECT_MAPPER.writeValueAsString(Map.of(
        "pair", response.getPair(),
        "value", response.getValue(),
        "timestamp", response.getTimestamp())).getBytes(StandardCharsets.UTF_8);

    return new MessageAndMetadata(body, Map.of("contentType", "application/json"));
  }
}
