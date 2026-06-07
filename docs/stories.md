# When working on this repository I was asking AI for USER STORIES. Below outputs are getting saved.

# [UC-01] Client registration
As an Insurance Agent,
I want to register a new client in the system by entering their basic details,
So that I can create a centralized profile to attach future insurance policies to.

Acceptance Criteria (AC):

AC1: The system must present a form capturing the client's First Name, Last Name, Email, and National ID (e.g., PESEL).

AC2: The system must validate that First Name, Last Name, and National ID are not empty.

AC3: The system must validate that the Email follows a standard format (e.g., user@domain.com).

AC4: Upon successful validation, the client record must be persisted in the relational database (PostgreSQL).

AC5: Upon successful creation, the system must automatically generate an immutable audit log entry (e.g., "Client [ID] registered") in the non-relational database (MongoDB).

AC6: The backend must return an HTTP 201 Created status with the new Client's ID.