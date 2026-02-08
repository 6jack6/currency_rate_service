# currency_rate_service

## How to run
1) Start server:

```bash
cd currency-rate-provider
./mvnw spring-boot:run
```

2) Start client in another terminal:

```bash
cd rate-printer
./mvnw spring-boot:run
```

You should see output like:
```
2026-02-08T17:05:36.456154Z USDRUB = 101,2238
```
