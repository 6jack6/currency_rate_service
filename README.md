# currency_rate_service

gRPC currency rate demo: ZooKeeper discovery, two provider replicas (`service1`, `service2`), Spring client (`client`), Pact Broker for contracts, plus Prometheus and Grafana.

## Prerequisites

Docker Engine running (e.g. Docker Desktop).

## Start everything

From the repo root:

```bash
docker compose up -d pact-broker-db pact-broker \
  && docker compose run --rm pact-consumer-tests \
  && docker compose up --build -d zookeeper service1 service2 client prometheus grafana
```

First run may take several minutes (images, Maven, Pact).

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

Recent lines only:

```bash
docker compose logs client --tail 100
```

## Grafana

Open [http://localhost:3000](http://localhost:3000). Login: **admin** / **admin**.

Use **Dashboards** → **JVM (Micrometer)** (provisioned from `monitoring/grafana/`). Use the **job** variable to switch targets (`client`, `service1`, `service2`, `zookeeper`).

## Prometheus

Open [http://localhost:9090](http://localhost:9090).

- **Status → Targets** — scrape health for `client`, `service1`, `service2`, `zookeeper`.
- **Graph** — ad-hoc PromQL; config lives in `monitoring/prometheus.yml`.

## Shell inside the client container

```bash
docker compose exec -it client sh
```

(Use `bash` instead of `sh` if the image provides it.)

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

Start again after `stop` without removing:

```bash
docker compose start zookeeper service1 service2 client prometheus grafana
```

Rebuild after code changes:

```bash
docker compose up --build -d zookeeper service1 service2 client prometheus grafana
```
