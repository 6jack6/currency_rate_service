package com.example.rateprinter;

import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.core.model.DefaultPactWriter;
import au.com.dius.pact.consumer.MessagePactBuilder;
import au.com.dius.pact.core.model.PactSpecVersion;
import au.com.dius.pact.core.model.V4Pact;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

class RatePrinterPactContractTest {
  private static final String CONSUMER = "rate-printer";
  private static final String PROVIDER = "currency-rate-provider";
  private static final Path PACT_DIR = Path.of("target", "pacts");
  private static final String DESCRIPTION = "get rate for requested currency pair";

  @Test
  void generatesAndOptionallyPublishesContract() throws Exception {
    Files.createDirectories(PACT_DIR);

    V4Pact pact = new MessagePactBuilder(PactSpecVersion.V4)
        .consumer(CONSUMER)
        .hasPactWith(PROVIDER)
        .given("rate for pair exists", Map.of("pair", "USDRUB"))
        .expectsToReceive(DESCRIPTION)
        .withContent(new PactDslJsonBody()
            .stringValue("pair", "USDRUB")
            .decimalType("value", 92.15)
            .stringMatcher(
                "timestamp",
                "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}(?:\\.\\d+)?Z$",
                Instant.parse("2026-03-10T10:15:30Z").toString()))
        .toPact(V4Pact.class);

    Path pactFile = PACT_DIR.resolve(CONSUMER + "-" + PROVIDER + ".json");
    DefaultPactWriter.INSTANCE.writePact(pactFile.toFile(), pact, PactSpecVersion.V4);

    assertTrue(Files.exists(pactFile), "Pact file should be generated");

    if (Boolean.getBoolean("pact.publish.enabled")) {
      publishPact(pactFile);
    }
  }

  private void publishPact(Path pactFile) throws IOException, InterruptedException {
    String brokerUrl = System.getProperty("pactbroker.url", "http://localhost:9292");
    String consumerVersion = System.getProperty("pact.consumer.version", "dev");

    HttpClient client = HttpClient.newHttpClient();
    HttpResponse<String> pactResponse = publishPactVersion(client, brokerUrl, consumerVersion, pactFile);
    if (pactResponse.statusCode() < 300) {
      return;
    }

    if (pactResponse.statusCode() == 409) {
      String fallbackVersion = fallbackVersion(consumerVersion, pactFile);
      HttpResponse<String> fallbackResponse = publishPactVersion(client, brokerUrl, fallbackVersion, pactFile);
      if (fallbackResponse.statusCode() < 300) {
        System.out.printf(
            "Consumer version %s already exists in broker with different content. Published pact as %s instead.%n",
            consumerVersion,
            fallbackVersion);
        return;
      }
      throw new IOException("Failed to publish pact to broker using fallback version " + fallbackVersion + ": "
          + fallbackResponse.statusCode() + " " + fallbackResponse.body());
    }

    throw new IOException("Failed to publish pact to broker: " + pactResponse.statusCode()
        + " " + pactResponse.body());
  }

  private HttpResponse<String> publishPactVersion(
      HttpClient client,
      String brokerUrl,
      String consumerVersion,
      Path pactFile
  ) throws IOException, InterruptedException {
    HttpRequest pactRequest = HttpRequest.newBuilder()
        .uri(URI.create(brokerUrl + "/pacts/provider/" + PROVIDER + "/consumer/" + CONSUMER
            + "/version/" + consumerVersion))
        .header("Content-Type", "application/json")
        .PUT(HttpRequest.BodyPublishers.ofFile(pactFile))
        .build();
    return client.send(pactRequest, HttpResponse.BodyHandlers.ofString());
  }

  private String fallbackVersion(String consumerVersion, Path pactFile) throws IOException {
    try {
      byte[] digest = MessageDigest.getInstance("SHA-256").digest(Files.readAllBytes(pactFile));
      StringBuilder suffix = new StringBuilder();
      for (int i = 0; i < 6; i++) {
        suffix.append(String.format("%02x", digest[i]));
      }
      return consumerVersion + "-" + suffix;
    } catch (NoSuchAlgorithmException e) {
      throw new IOException("SHA-256 algorithm is not available", e);
    }
  }
}
