# currency_rate_service

gRPC currency rate demo: ZooKeeper discovery, two provider replicas (`service1`, `service2`), Spring client (`client`), Pact Broker for contracts, plus Prometheus and Grafana.

## Prerequisites

Docker Engine running (e.g. Docker Desktop).

## Build -> Release -> Run (strict separation)

From the repo root:

1) Build stage (build images only):

```bash
docker compose -f docker-compose.build.yml build service1 client
```

2) Release stage (prepare runtime tags):

```bash
./scripts/release-images.sh
```

3) Runtime dependencies for contract tests:

```bash
docker compose up -d pact-broker-db pact-broker
```

4) Contract tests (consumer):

```bash
docker compose run --rm pact-consumer-tests
```

5) Contract tests (provider):

```bash
docker compose run --rm pact-provider-tests
```

6) Run stage (start runtime services from release images):

```bash
docker compose up -d zookeeper service1 service2 client prometheus grafana
```

The first build can take several minutes (Maven + image layers).

## Logs

Follow all services:

```bash
docker compose logs -f
```

Follow one service (e.g. client):

```bash
docker compose logs -f client
```

Other useful names: `service1`, `service2`, `zookeeper`, `prometheus`, `grafana`, `pact-broker`.

## Grafana

Open [http://localhost:3000](http://localhost:3000). Login: **admin** / **admin**.

Use **Dashboards** → **JVM (Micrometer)** (from `monitoring/grafana/`). Use the **job** variable to switch targets (`client`, `service1`, `service2`, `zookeeper`).

## Prometheus

Open [http://localhost:9090](http://localhost:9090).

- **Status → Targets** — scrape health for `client`, `service1`, `service2`, `zookeeper`.
- **Graph** — config lives in `monitoring/prometheus.yml`.


## Stop containers

Stop runtime + monitoring (leave Pact Broker up):

```bash
docker compose stop zookeeper service1 service2 client prometheus grafana
```

Stop the full stack (all compose services):

```bash
docker compose stop
```

## Remove containers

Remove containers and default networks (keeps named volumes such as Pact Postgres data):

```bash
docker compose down
```

Also remove volumes (e.g. broker DB):

```bash
docker compose down -v --remove-orphans
```

Remove images used by the compose file as well:

```bash
docker compose down --rmi all -v --remove-orphans
```

Rebuild after code changes:

```bash
docker compose -f docker-compose.build.yml build service1 client
./scripts/release-images.sh
docker compose up -d pact-broker-db pact-broker
docker compose run --rm pact-consumer-tests
docker compose run --rm pact-provider-tests
docker compose up -d zookeeper service1 service2 client prometheus grafana
```
