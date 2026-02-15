package com.example.gateway.routesconfig;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

@Configuration
public class GatewayRoute {

    @Bean
    public RedisRateLimiter  redisRateLimiter() {
        return new RedisRateLimiter(20,40,1);
    }

    @Bean
    public KeyResolver keyResolver() {
        return exchange -> Mono.just(
                exchange
                        .getRequest()
                        .getRemoteAddress()
                        .getAddress()
                        .getHostAddress()
        );
    }

    @Bean
    public RouteLocator customRoute(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("user", r -> r.path("/api/users/**")
                        .filters(f-> f
                                .requestRateLimiter(rt-> rt
                                        .setRateLimiter(redisRateLimiter())
                                        .setKeyResolver(keyResolver()))
                                .circuitBreaker(breaker -> breaker
                                        .setName("userCircuitBreaker")
                                        .setFallbackUri("forward:/fallback/users"))
                        )
                        .uri("lb://USER"))

                .route("jobs", r -> r.path("/api/jobs/**")
                        .filters(f-> f
                                .requestRateLimiter(rt-> rt
                                        .setRateLimiter(redisRateLimiter())
                                        .setKeyResolver(keyResolver()))
                                .circuitBreaker(breaker-> breaker
                                        .setName("jobCircuitBreaker")
                                        .setFallbackUri("forward:/fallback/jobs"))
                                )
                        .uri("lb://JOB"))

                .route("review", r -> r.path("/api/reviews/**")
                        .filters(f-> f
                                .requestRateLimiter(rt-> rt
                                        .setRateLimiter(redisRateLimiter())
                                        .setKeyResolver(keyResolver()))
                                .circuitBreaker(breaker-> breaker
                                        .setName("reviewCircuitBreaker")
                                        .setFallbackUri("forward:/fallback/reviews")))
                        .uri("lb://REVIEW"))

                .route("submitapplication", r-> r.path("/api/application/**")
                        .filters(f-> f
                                .requestRateLimiter(rt-> rt
                                        .setRateLimiter(redisRateLimiter())
                                        .setKeyResolver(keyResolver()))
                                .circuitBreaker(breaker-> breaker
                                        .setName("submitCircuitBreaker")
                                        .setFallbackUri("forward:/fallback/application")))
                        .uri("lb://SUBMITAPPLICATION"))

                .route("company", r-> r.path("/api/companies/**")
                        .filters(f-> f
                                .requestRateLimiter(rt-> rt
                                        .setRateLimiter(redisRateLimiter())
                                        .setKeyResolver(keyResolver()))
                                .circuitBreaker(breaker-> breaker
                                        .setName("companyCircuitBreaker")
                                        .setFallbackUri("forward:/fallback/company")))
                        .uri("lb://COMPANY"))

                .route("eureka-server",r -> r
                        .path("/eureka/main")
                        .filters(s -> s.rewritePath("/eureka/main","/"))
                        .uri("http://localhost:8761"))
                .route("eureka-server-static",r -> r
                        .path("/eureka/**")
                        .uri("http://localhost:8761"))
                .build();

    }
}
