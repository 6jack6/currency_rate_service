#!/usr/bin/env sh
set -eu

ROOT_DIR="$(cd "$(dirname "$0")/.." && pwd)"
cd "$ROOT_DIR"

log_stage() {
  printf '\n==== %s ====\n' "$1"
}

retry_cmd() {
  attempts="$1"
  shift
  i=1
  while [ "$i" -le "$attempts" ]; do
    if "$@"; then
      return 0
    fi
    if [ "$i" -lt "$attempts" ]; then
      printf 'Command failed (attempt %s/%s), retrying in 5s...\n' "$i" "$attempts"
      sleep 5
    fi
    i=$((i + 1))
  done
  return 1
}

log_stage "BUILD"
retry_cmd 5 docker compose -f docker-compose.build.yml build service1 client

log_stage "RELEASE"
"$ROOT_DIR/scripts/release-images.sh"

log_stage "BROKER"
retry_cmd 5 docker compose pull pact-broker-db pact-broker
retry_cmd 5 docker compose up -d pact-broker-db pact-broker

log_stage "CONTRACT TESTS: CONSUMER"
retry_cmd 3 docker compose run --rm pact-consumer-tests

log_stage "CONTRACT TESTS: PROVIDER"
retry_cmd 3 docker compose run --rm pact-provider-tests

log_stage "RUN"
retry_cmd 5 docker compose up -d zookeeper service1 service2 client prometheus grafana

log_stage "VERIFY"
docker compose ps
