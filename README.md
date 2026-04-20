# currency_rate_service

gRPC currency rate demo: ZooKeeper discovery, two provider replicas (`service1`, `service2`), Spring client (`client`), Pact Broker for contracts, plus Prometheus and Grafana.

## Environment split

The stack is now split by environment via dedicated env files and Compose project names:

- `dev` -> `.env.dev` (`COMPOSE_PROJECT_NAME=currency-rate-service-dev`)
- `staging` -> `.env.staging` (`COMPOSE_PROJECT_NAME=currency-rate-service-staging`)
- `production` -> `.env.prod` (`COMPOSE_PROJECT_NAME=currency-rate-service-prod`)

This gives hard runtime isolation (containers/networks/volumes) between environments, while all three still use the same `docker-compose.yml`.

## Prerequisites

Docker Engine running (e.g. Docker Desktop).

## Build -> Release -> Run (dev pipeline)

From the repo root:

```bash
./scripts/build-release-run.sh
```

The script uses `.env.dev` by default. To run with another env file:

```bash
ENV_FILE=.env.staging ./scripts/build-release-run.sh
```

What this pipeline does:

1. Builds app images (`docker-compose.build.yml`)
2. Creates release tags (`scripts/release-images.sh`)
3. Starts Pact Broker infra (`contracts` profile)
4. Runs consumer/provider contract tests (`contracts` profile)
5. Starts runtime services (`zookeeper`, `service1`, `service2`, `client`, `prometheus`, `grafana`)

## Run runtime only (no contracts)

`dev`:

```bash
docker compose --env-file .env.dev -p currency-rate-service-dev up -d zookeeper service1 service2 client prometheus grafana
```

`staging`:

```bash
docker compose --env-file .env.staging -p currency-rate-service-staging up -d zookeeper service1 service2 client prometheus grafana
```

`production`:

```bash
docker compose --env-file .env.prod -p currency-rate-service-prod up -d zookeeper service1 service2 client prometheus grafana
```

## Contracts profile

Pact services/tests are separated into the `contracts` profile and are not required for runtime startup.

Start Pact Broker infra (example: dev):

```bash
docker compose --env-file .env.dev -p currency-rate-service-dev --profile contracts up -d pact-broker-db pact-broker
```

Run tests (example: dev):

```bash
docker compose --env-file .env.dev -p currency-rate-service-dev --profile contracts run --rm pact-consumer-tests
docker compose --env-file .env.dev -p currency-rate-service-dev --profile contracts run --rm pact-provider-tests
```

## Logs

Follow all services (example: dev):

```bash
docker compose --env-file .env.dev -p currency-rate-service-dev logs -f
```

Follow one service (example: client):

```bash
docker compose --env-file .env.dev -p currency-rate-service-dev logs -f client
```

## Grafana and Prometheus

Ports are environment-specific and defined in `.env.*`.

Default values:

- `dev`: Grafana `23000`, Prometheus `29090`
- `staging`: Grafana `33000`, Prometheus `39090`
- `prod`: Grafana `3000`, Prometheus `9090`

## Stop and remove

Stop (example: prod):

```bash
docker compose --env-file .env.prod -p currency-rate-service-prod stop
```

Remove containers/networks (example: prod):

```bash
docker compose --env-file .env.prod -p currency-rate-service-prod down
```

Remove containers/networks/volumes (example: prod):

```bash
docker compose --env-file .env.prod -p currency-rate-service-prod down -v --remove-orphans
```
