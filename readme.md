# Insurance Industry POC (Mock Project)

This is a POC project that resembles a few general concepts used in insurance industry systems.

It is intended for learning, demos, and experimentation only.

## What this project consists of

- Frontend: Angular UI for client registration flow.
- REST API: Spring Boot endpoints for handling client operations.
- Backend domain logic: services, repositories, and event handling.
- Database 1: PostgreSQL for relational insurance data (clients, policies, payments).
- Database 2: MongoDB for document-oriented data (e.g., logs/audit-style records).
- Infrastructure: Docker Compose setup for local databases.

## High-level architecture

```mermaid
flowchart LR
    Browser[Angular frontend]

    subgraph BE[Backend Runtime]
        API[Java Backend]
        E1[POST /api/v1/clients]
        E2[GET /api/v1/external-insurance/users/externalUserId]
        E3[PUT /api/v1/external-insurance/policies/policyId/status]
        E4[GET /api/v1/system-properties/customerId/propertyKey]
        E5[PUT /api/v1/system-properties/customerId/propertyKey]
    end

    subgraph Infra[Local Infrastructure via Docker Compose]
        PG[(PostgreSQL\npolicytracker)]
        MG[(MongoDB\npolicytracker)]
    end

    Mock[External Service]

    Browser -->|HTTP /api/v1/*| API
    API --> E1
    API --> E2
    API --> E3
    API --> E4
    API --> E5

    E1 -->|register client| PG
    E2 -->|fetch insurance user data| Mock
    E3 -->|update policy status| Mock
    E4 -->|read customer property| PG
    E5 -->|upsert customer property| PG

    API -->|audit events| MG
```

## Backend tree

```text
backend/
├─ client/             REST API call handling
├─ audit/              MongoDB-backed audit event storage
├─ externalinsurance/  OpenAPI-based external service communication
├─ events/             domain event contracts/publishing
├─ requestcontext/     request-scoped user context + exception handling
├─ systemproperties/   JPA/PostgreSQL customer-scoped properties
├─ referencedataimport/ CSV import + tree building
└─ common/             shared contracts/markers
```
