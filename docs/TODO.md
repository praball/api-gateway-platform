# Project TODO

This file tracks project-level tasks and short-term roadmap items. Use GitHub task lists for quick status updates: mark completed items with - [x] or open a related Issue for fuller discussion.

Conventions
- Use task list syntax: `- [ ]` open, `- [x]` done.
- Optionally add owner and target date: `- [ ] Task (owner @username, ETA: 2026-08-01)`.
- Keep this file high-level; use Issues for detailed work, PRs for implementation.


------------------------------------------------------------------------

## Backlog:
- [ ] multi-stage build in Dockerfile
- [ ] Custom API Gateway filters
- [ ] Kafka async audit logging
- [ ] Request tracing (Trace ID / Correlation ID)
- [ ] Micrometer + Prometheus + Grafana + New Relic
- [ ] Config Server
- [ ] Kubernetes
- [ ] MCP Server
- [ ] LLM-powered admin interface

## Done:
- [x] Add Actual Endpoints with Services
- [x] Add @RestControllerAdvice
- [x] Add DTOs
- [x] Add Validations
- [x] API request response model
- [x] Use @Transactional for service layer
- [x] Add Swagger for API documentation
- [x] Add Bean validation
- [x] Add docker-compose.yaml
- [x] JWT authentication & authorization
- [x] Role-based routing
- [x] Redis-backed distributed rate limiting
- [x] Multiple algorithms (Token Bucket, Sliding Window, Leaky Bucket)

------------------------------------------------------------------------

### How to use
1. Edit this file on a branch for larger changes or update as quick notes directly in the repo.
2. For tasks requiring multiple steps, create an Issue and link it here: `- [ ] Task (see #123)`.
3. Prefer Issues+Projects for assignment, tracking, and milestones.

### Notes:
- Keep entries concise. Remove or move completed items to the Done section occasionally to keep the backlog readable.
