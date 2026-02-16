# currency_rate_service

## Prerequisite

Start ZooKeeper on `localhost:2181`.

Example with Docker:

```bash
docker run --name zookeeper -p 2181:2181 -d zookeeper:3.9
```

## How to run

1) Start first provider instance:

```bash
cd currency-rate-provider
./mvnw spring-boot:run
```

2) Start second provider instance (optional, to see balancing):

```bash
cd currency-rate-provider
GRPC_PORT=9091 ./mvnw spring-boot:run
```

3) Start client in another terminal:

```bash
cd rate-printer
./mvnw spring-boot:run
```

`rate-printer` will discover all `currency-rate-provider` instances from ZooKeeper and distribute gRPC calls with round-robin balancing.
