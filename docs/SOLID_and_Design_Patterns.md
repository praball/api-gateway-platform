# SOLID Principles & Design Patterns

## SOLID Principles

### 1. Single Responsibility Principle (SRP)

-   Controllers handle HTTP requests.
-   Services contain business logic.
-   Repositories handle persistence.
-   Filters handle authentication/rate limiting.
-   JWT utility only creates and validates JWTs.

### 2. Open/Closed Principle (OCP)

-   New rate-limiting algorithms can be added by implementing
    `RateLimiter` without modifying existing algorithms.

### 3. Liskov Substitution Principle (LSP)

-   `FixedWindowRateLimiter`, `SlidingWindowRateLimiter`, and
    `TokenBucketRateLimiter` can all be used wherever `RateLimiter` is
    expected.

### 4. Interface Segregation Principle (ISP)

-   Small interfaces such as `RateLimiter`, `UserService`, and
    `OrderService` expose only required operations.

### 5. Dependency Inversion Principle (DIP)

-   `RateLimitFilter` depends on the `RateLimiter` abstraction rather
    than concrete implementations.
-   Services depend on interfaces instead of implementations.

------------------------------------------------------------------------

# Design Patterns

## 1. Strategy Pattern ✅

**Where:** `RateLimiter` interface

Implementations: - `FixedWindowRateLimiter` -
`SlidingWindowRateLimiter` - `TokenBucketRateLimiter`

Allows switching algorithms without changing gateway code.

------------------------------------------------------------------------

## 2. Factory Pattern (Configuration-based) ✅

**Where:** `RateLimiterConfig`

A Spring `@Configuration` class selects the desired `RateLimiter`
implementation based on:

``` properties
rate-limit.algorithm=fixed
rate-limit.algorithm=sliding
rate-limit.algorithm=token
```

The selected implementation is exposed as the application's
`RateLimiter` bean.

------------------------------------------------------------------------

## 3. Dependency Injection (IoC) ✅

Used throughout the project via constructor injection
(`@RequiredArgsConstructor`) and Spring-managed beans.

------------------------------------------------------------------------

## 4. Repository Pattern ✅

Spring Data JPA repositories abstract database access.

Examples: - `UserRepository` - `OrderRepository`

------------------------------------------------------------------------

## 5. Proxy Pattern (Framework) ✅

Spring Cloud Gateway acts as a reverse proxy, forwarding requests to
downstream microservices while applying authentication, authorization,
and rate limiting.

------------------------------------------------------------------------

## 6. Filter Pattern ✅

Gateway filters process requests before they reach services.

Examples: - `AuthenticationFilter` - `RateLimitFilter`

------------------------------------------------------------------------

## Summary

### SOLID

-   ✅ SRP
-   ✅ OCP
-   ✅ LSP
-   ✅ ISP
-   ✅ DIP

### Design Patterns

-   ✅ Strategy Pattern
-   ✅ Factory Pattern (configuration-based)
-   ✅ Dependency Injection / IoC
-   ✅ Repository Pattern
-   ✅ Proxy Pattern
-   ✅ Filter Pattern
