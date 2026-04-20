#!/usr/bin/env sh
set -eu

docker image inspect currency-rate-provider:build >/dev/null
docker image inspect rate-printer:build >/dev/null

docker tag currency-rate-provider:build currency-rate-provider:release
docker tag rate-printer:build rate-printer:release

echo "Release images are prepared: currency-rate-provider:release, rate-printer:release"
