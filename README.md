# currency_rate_service

## One-command start (Pact + runtime)

Run Pact Broker, publish consumer contracts, and then start all runtime services:

```bash
docker compose up -d pact-broker-db pact-broker && docker compose run --rm pact-consumer-tests && docker compose up --build -d zookeeper currency-rate-provider rate-printer
```

## Prerequisite

Start Docker Desktop (or any running Docker Engine).

Consumer contracts are generated in `rate-printer/target/pacts` and can be published to the broker.
`currency-rate-provider` verifies contracts directly from Pact Broker during Docker image build.

## How to run

Start Pact Broker:

```bash
docker compose up -d pact-broker-db pact-broker
```

Generate and publish consumer pact:

```bash
docker compose run --rm pact-consumer-tests
```

Build and start runtime services:

```bash
docker compose up --build -d zookeeper currency-rate-provider rate-printer
```

Watch client output (rates every ~5 seconds):

```bash
docker compose logs -f rate-printer
```

## Cleanup

Stop and remove everything:

```bash
docker compose down -v --remove-orphans
```

If you also want to remove downloaded Docker images for this project:

```bash
docker compose down --rmi all -v --remove-orphans
```

## What Uses What

- `Pact Broker` stores published consumer contracts and provider verification results. `rate-printer` publishes to it,
  `currency-rate-provider` loads contracts from it during build (`mvn verify`).
- `ZooKeeper` is unrelated to Pact. It is used only for runtime service discovery between the gRPC client and provider
  instances.
