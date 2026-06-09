# External Insurance Mock Service

Minimal Java service to emulate the external insurance provider used by backend `externalinsurance`.

## Run

```bash
mvn -f tools/externalService/pom.xml exec:java
```

Optional env vars:

- `PORT` (default: `18080`)

## Endpoints

- `POST /mock-external-insurance/insurance/user-data`
- `PUT /mock-external-insurance/insurance/status`

Also accepts the same endpoints under `/v1` and without prefix.
