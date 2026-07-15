# Project TODO

This file tracks project-level tasks and short-term roadmap items. Use GitHub task lists for quick status updates: mark completed items with - [x] or open a related Issue for fuller discussion.

Conventions
- Use task list syntax: `- [ ]` open, `- [x]` done.
- Optionally add owner and target date: `- [ ] Task (owner @username, ETA: 2026-08-01)`.
- Keep this file high-level; use Issues for detailed work, PRs for implementation.


------------------------------------------------------------------------

## Backlog:
- [ ] Config Server
- [ ] Kubernetes
- [ ] MCP Server
- [ ] LLM-powered admin interface

## Done:
- [x] Add Actual Endpoints with Services
- [x] Add @RestControllerAdvice
- [x] Add DTOs
- [x] Add Validations
- [x] Add API request response model
- [x] Add Postgres database persistence
- [x] Use @Transactional for service layer
- [x] Add Swagger for API documentation
- [x] Add Bean validation
- [x] Add docker-compose.yaml
- [x] Add JWT authentication & authorization
- [x] Add Role-based routing
- [x] Add Redis-backed distributed rate limiting
- [x] Add Multiple algorithms (Token Bucket, Sliding Window, Leaky Bucket)
- [x] Add Custom API Gateway filters
- [x] Add Kafka async audit logging with persistence
- [x] Add Kafka DLT/Message Poisoning handling.
- [x] Request tracing (Trace ID / Correlation ID)
- [x] Add Micrometer + Prometheus + Grafana + New Relic APM integration

------------------------------------------------------------------------

### How to use
1. Edit this file on a branch for larger changes or update as quick notes directly in the repo.
2. For tasks requiring multiple steps, create an Issue and link it here: `- [ ] Task (see #123)`.
3. Prefer Issues+Projects for assignment, tracking, and milestones.

### Notes:
- Keep entries concise. Remove or move completed items to the Done section occasionally to keep the backlog readable.
