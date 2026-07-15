# New Relic APM Integration

Comprehensive observability for the API Gateway Platform with real-time performance monitoring, error tracking, and distributed tracing.

## Illustration

![New Relic Dashboard](./docs/images/dashboard_newrelic.png)

## Overview

| Aspect | Details |
|--------|---------|
| **Service Type** | APM Monitoring |
| **Platform** | New Relic Java Agent |
| **Coverage** | All microservices (Gateway, Order, User, Discovery, Config, Audit) |
| **Env Required** | `NEW_RELIC_LICENSE_KEY` |

## Setup

### Prerequisites
- New Relic account with Java APM license
- Java 8+ runtime  
- Network access to New Relic collector endpoints

### Installation Steps

1. **Download New Relic Agent**
   ```bash
   curl -O https://download.newrelic.com/newrelic/java-agent/newrelic-java-latest.tar.gz
   tar xfz newrelic-java-latest.tar.gz -C newrelic/
   ```

2. **Extract** `newrelic.jar` to this directory:
   ```
   newrelic/
   ├── newrelic.jar
   ├── newrelic.yml
   └── README.md
   ```

3. **Configure License Key** - Create `.env`:
   ```bash
   NEW_RELIC_LICENSE_KEY=your_40_character_key_here
   ```

4. **Enable Agent** - Add to JVM startup:
   ```bash
   -javaagent:newrelic/newrelic.jar
   ```

## Monitoring & Dashboards

Access at [https://one.newrelic.com/](https://one.newrelic.com/)

**Key Sections:**
- **APM Overview**: Service health, response times, throughput
- **Errors**: Error rate, error traces, stack traces
- **Transactions**: Transaction performance, slowest queries
- **Databases**: Connection pool monitoring, slow queries
- **Distributed Tracing**: Cross-service request flows

## Architecture

```
┌──────────────────────────────────┐
│   New Relic APM Collector        │
└────────────────┬─────────────────┘
                 │
    ┌────────────┼────────────┐
    │            │            │
┌───▼──┐    ┌───▼──┐    ┌───▼──┐
│Agent │    │Agent │    │Agent │
└──────┘    └──────┘    └──────┘
   │          │          │
┌──▼─────────▼──────────▼──────┐
│   API Gateway Platform        │
│   (Microservices)             │
└──────────────────────────────┘
```

## Configuration

See `newrelic.yml` for detailed settings:
- **App Name**: `api-gateway-platform`
- **Environment**: Auto-detected from `APP_ENV`
- **High Security Mode**: `false` (set `true` for production)
- **Transaction Tracing**: Enabled by default

## Troubleshooting

| Issue | Cause | Solution |
|-------|-------|----------|
| Agent not connecting | Invalid LICENSE_KEY | Verify key in `.env` (40 chars) |
| No data in UI | Firewall blocking | Allow outbound to New Relic endpoints |
| High memory usage | Large app with many transactions | Increase JVM heap or reduce sampling |
| Slow startup | Agent initialization overhead | Normal (30-60s on first start) |

**Debug logs**: Check `newrelic/` directory for `newrelic_agent.log`

## Best Practices

- **Naming**: Keep app name consistent across services
- **Custom Events**: Track business metrics (e.g., order placed)
- **Alerts**: Set up alerts for error rate, response time anomalies
- **Retention**: Free tier has 24-hour retention; upgrade for longer history

## Links

- [New Relic Java Agent Docs](https://docs.newrelic.com/docs/agents/java-agent)
- [Agent Configuration](https://docs.newrelic.com/docs/agents/java-agent/configuration/java-agent-configuration-config-file)
- [Custom Instrumentation](https://docs.newrelic.com/docs/agents/java-agent/custom-instrumentation/custom-instrumentation-editor-quickly-customize-your-java-instrument)
