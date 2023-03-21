package com.kakaobank.blog.client.external

import com.kakaobank.blog.client.error.BlogAPIClientErrorCode
import com.kakaobank.blog.client.error.BlogAPIClientException
import io.github.resilience4j.circuitbreaker.CallNotPermittedException
import org.springframework.cloud.openfeign.FallbackFactory

open class DefaultFeignFallbackFactory<T> : FallbackFactory<T> {

    override fun create(cause: Throwable?): T {
        if (cause is CallNotPermittedException) {
            throw BlogAPIClientException(BlogAPIClientErrorCode.CircuitBreakOpened)
        } else {
            throw cause ?: BlogAPIClientException(BlogAPIClientErrorCode.CircuitBreakOpened)
        }
    }
}