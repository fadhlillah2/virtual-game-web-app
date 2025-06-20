# Implement Distributed Tracing with Micrometer Tracing and Zipkin

## Status

Accepted

## Context

As our application grows in complexity with multiple services and components, it becomes increasingly difficult to understand the flow of requests and identify performance bottlenecks. We need a way to trace requests as they flow through our system, especially for critical operations like chip transactions, which are core to our virtual casino application.

The current monitoring setup includes Prometheus and Grafana for metrics, but lacks the ability to trace requests across different components of the application. This makes it challenging to:

1. Debug performance issues in production
2. Understand the dependencies between services
3. Identify slow operations in the request flow
4. Correlate logs from different services for a single request

## Decision

We will implement distributed tracing using Micrometer Tracing (the successor to Spring Cloud Sleuth in Spring Boot 3.x) with Zipkin as the tracing backend. This implementation includes:

1. Adding Micrometer Tracing dependencies to the project
2. Configuring Zipkin in application properties for all environments (dev, test, prod)
3. Adding custom tracing to key components, particularly the ChipService which handles critical operations

For custom tracing, we'll use the Observation API provided by Micrometer Tracing to create spans for important business operations. This will allow us to:

- Track the execution of key methods
- Add contextual information to traces (user IDs, transaction types, amounts)
- Correlate traces with logs and metrics

## Consequences

### Positive

- Improved observability of the application, making it easier to identify performance bottlenecks
- Better debugging capabilities in production environments
- Ability to trace requests across different components of the application
- Enhanced monitoring of critical business operations like chip transactions
- Integration with existing monitoring tools (Prometheus and Grafana)

### Negative

- Slight increase in overhead due to tracing instrumentation
- Additional infrastructure component (Zipkin) to maintain
- Need to ensure sensitive data is not included in traces

## Alternatives Considered

1. **OpenTelemetry**: While OpenTelemetry is becoming the industry standard for observability, Micrometer Tracing provides better integration with Spring Boot 3.x and our existing monitoring stack. We may consider migrating to OpenTelemetry in the future as it matures.

2. **Jaeger**: Jaeger is another popular distributed tracing system. We chose Zipkin because it has better integration with Spring Boot and is simpler to set up. Jaeger might be considered in the future if we need more advanced features.

3. **Custom logging solution**: We could implement a custom solution using correlation IDs in logs. However, this would require significant development effort and would not provide the visualization capabilities of a dedicated tracing system.

## References

- [Micrometer Tracing Documentation](https://micrometer.io/docs/tracing)
- [Zipkin Documentation](https://zipkin.io/pages/documentation)
- [Spring Boot 3.x Observability Documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/actuator.html#actuator.observability)
- [Distributed Tracing with Spring Cloud Sleuth and Zipkin](https://spring.io/blog/2016/02/15/distributed-tracing-with-spring-cloud-sleuth-and-spring-cloud-zipkin)
