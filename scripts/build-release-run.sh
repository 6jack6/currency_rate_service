#!/usr/bin/env sh
set -eu

ROOT_DIR="$(cd "$(dirname "$0")/.." && pwd)"
cd "$ROOT_DIR"

ENV_FILE="${ENV_FILE:-.env.dev}"

if [ ! -f "$ENV_FILE" ]; then
  echo "Env file not found: $ENV_FILE" >&2
  exit 1
fi

PROJECT_NAME="$(
  sed -n 's/^COMPOSE_PROJECT_NAME=//p' "$ENV_FILE" | head -n 1
)"
PROJECT_NAME="${PROJECT_NAME:-currency-rate-service-dev}"

log_stage() {
  printf '\n==== %s ====\n' "$1"
}

compose_cmd() {
  docker compose --env-file "$ENV_FILE" -p "$PROJECT_NAME" "$@"
}

log_stage "BUILD"
compose_cmd -f docker-compose.build.yml build service1 client

log_stage "RELEASE"
"$ROOT_DIR/scripts/release-images.sh"

log_stage "BROKER"
compose_cmd --profile contracts pull pact-broker-db pact-broker
compose_cmd --profile contracts up -d pact-broker-db pact-broker

log_stage "CONTRACT TESTS: CONSUMER"
compose_cmd --profile contracts run --rm pact-consumer-tests

log_stage "CONTRACT TESTS: PROVIDER"
compose_cmd --profile contracts run --rm pact-provider-tests

log_stage "RUN"
compose_cmd up -d zookeeper service1 service2 client prometheus grafana

log_stage "VERIFY"
compose_cmd ps
