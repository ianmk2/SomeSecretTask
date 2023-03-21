package com.kakaobank.blog.client

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig.SlidingWindowType
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig.custom
import io.github.resilience4j.timelimiter.TimeLimiterConfig
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder
import org.springframework.cloud.client.circuitbreaker.Customizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.Duration

@Configuration
class CircuitBreakerConfig {

    @Bean
    fun defaultCustomizer(): Customizer<Resilience4JCircuitBreakerFactory> {
        return Customizer<Resilience4JCircuitBreakerFactory> { factory ->
            factory.configureDefault { id ->
                Resilience4JConfigBuilder(id)
                    .timeLimiterConfig(
                        TimeLimiterConfig.custom()
                            .timeoutDuration(Duration.ofSeconds(5))
                            .build()
                    )
                    .circuitBreakerConfig(
                        custom()
                            //에러율 50%시 OPEN
                            .failureRateThreshold(50f)
                            .enableAutomaticTransitionFromOpenToHalfOpen()
                            .slidingWindowType(SlidingWindowType.COUNT_BASED)
                            .slidingWindowSize(5)
                            .minimumNumberOfCalls(3)
                            //OPEN상태를 최소 10초 유지 후 HALF_OPEN으로 변경
                            .waitDurationInOpenState(Duration.ofSeconds(10))
                            .build()
                    )
                    .build()
            }
        }
    }
}