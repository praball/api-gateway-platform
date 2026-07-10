# Redis-backed Distributed Rate Limiting

## Overview

Rate limiting protects APIs from abuse by restricting how many requests a client can make within a given time period.

In this project, rate limiting is implemented at the **API Gateway**, so every request is checked before reaching downstream microservices.

Redis is used as a centralized store, allowing all gateway instances to share the same rate limit state.

---

# Why Redis?

Without Redis:

* Each gateway instance maintains its own counter.
* Clients can bypass limits by hitting different gateway instances.
* Counters are lost on application restart.

With Redis:

* Shared counters across all gateway instances.
* Fast in-memory operations.
* Automatic key expiration.
* Horizontally scalable.

---

# Request Flow

Client
→ API Gateway
→ RateLimitFilter
→ Redis
→ Allowed / Rejected
→ User / Order Service

For every incoming request:

1. Identify the client (JWT username in this project).
2. Read/update rate limit data in Redis.
3. If the limit is exceeded, return **HTTP 429 (Too Many Requests)**.
4. Otherwise, forward the request to the target microservice.

---

# 1. Fixed Window Algorithm

## Concept

Maintain a counter for a fixed time window.

Example:

* Limit: **5 requests**
* Window: **10 seconds**

Redis stores:

rate:admin → 3

Every request:

* Increment counter.
* If counter == 1, set expiry to 10 seconds.
* Reject once counter exceeds 5.

### Example

Window: 0–10 seconds

Request #1 ✔

Request #2 ✔

Request #3 ✔

Request #4 ✔

Request #5 ✔

Request #6 ❌ 429

At 10 seconds, Redis automatically deletes the key and counting starts again.

### Pros

* Very simple.
* Fast.
* Easy to implement.

### Cons

Boundary burst problem.

Example:

9.9s → 5 requests

10.1s → 5 requests

Result: 10 requests within ~0.2 seconds.

---

# 2. Sliding Window Algorithm

## Concept

Instead of storing one counter, store the timestamp of every request.

On every request:

1. Remove timestamps older than the window.
2. Count remaining timestamps.
3. If count ≥ limit → reject.
4. Otherwise, add current timestamp.

Redis uses a **Sorted Set (ZSET)**.

Example:

Current window = last 10 seconds

Stored timestamps:

1s

3s

5s

8s

Current request: 9s

Count = 4

Allow request | Store timestamp 9s.

Later...

Current request: 11s

Remove timestamp 1s.

Remaining:

3

5

8

9

Count = 4 | Allow.

The window continuously slides instead of resetting.

### Redis Commands

* ZADD
* ZCARD / ZCOUNT
* ZREMRANGEBYSCORE

### Pros

* Very accurate.
* Eliminates boundary burst problem.

### Cons

* Slightly higher memory usage.
* More Redis operations.

---

# 3. Token Bucket Algorithm

## Concept

Imagine a bucket filled with tokens. Each request consumes one token.

Tokens are replenished at a fixed rate.

Example:

Bucket capacity = 5

Refill rate = 1 token every 2 seconds.

Initial bucket:

★★★★★

Five immediate requests are allowed.

Bucket becomes empty.

Next request:

❌ 429

After waiting 2 seconds:

★

One request is allowed again.

### Pros

* Allows short bursts.
* Smooth average traffic.
* Widely used in production API gateways.

### Cons

* Slightly more complex implementation.

---

# 4. Leaky Bucket Algorithm

## Concept

Incoming requests are placed into a queue.

Requests leave the queue at a constant rate.

Example:

Incoming:

100 requests instantly.

Outgoing:

1 request/second.

Traffic becomes smooth regardless of burst size.

### Pros

* Smooth traffic.
* Protects downstream systems.

### Cons

* Can increase latency.
* Requests may be dropped if the queue becomes full.

---

# Algorithm Comparison

| Algorithm      | Burst Handling | Accuracy | Complexity | Common Usage                 |
| -------------- | -------------- | -------- | ---------- | ---------------------------- |
| Fixed Window   | Poor           | Low      | Easy       | Small projects               |
| Sliding Window | Good           | High     | Medium     | Modern APIs                  |
| Token Bucket   | Excellent      | High     | Medium     | API Gateways, Cloud Services |
| Leaky Bucket   | Excellent      | High     | Medium     | Network Traffic Shaping      |


---

# Key Takeaways

* Redis enables distributed rate limiting across multiple gateway instances.
* Fixed Window is simple but suffers from burst issues.
* Sliding Window provides more accurate request counting.
* Token Bucket is the preferred algorithm for most production API gateways.
* Leaky Bucket is useful when traffic needs to be processed at a constant rate.
