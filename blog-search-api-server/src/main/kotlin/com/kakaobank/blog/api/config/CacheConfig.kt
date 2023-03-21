package com.kakaobank.blog.api.config

import com.github.benmanes.caffeine.cache.Caffeine
import com.kakaobank.blog.client.external.kakao.DefaultKakaoBlogSearchClientWrapperFactory
import com.kakaobank.blog.client.external.naver.DefaultNaverBlogSearchClientWrapperFactory
import mu.KotlinLogging
import org.apache.commons.lang3.exception.ExceptionUtils
import org.springframework.cache.Cache
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.CachingConfigurer
import org.springframework.cache.caffeine.CaffeineCache
import org.springframework.cache.interceptor.CacheErrorHandler
import org.springframework.cache.interceptor.CacheResolver
import org.springframework.cache.support.SimpleCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.concurrent.TimeUnit

object CacheNames {
    const val PopularSearchTerm = "PopularSearchTerm"
}

object CacheDuration {
    const val PopularSearchTerm = 10L
    const val BlogClientResult = 60L
}

@Configuration
class CacheConfig : CachingConfigurer {


    @Bean
    override fun cacheManager(): CacheManager? {
        val cacheManager = SimpleCacheManager()
        val caches = listOf(
            buildLocalMemoryCache(CacheNames.PopularSearchTerm, CacheDuration.PopularSearchTerm),
            buildLocalMemoryCache(DefaultKakaoBlogSearchClientWrapperFactory.KakaoClientSearchCacheName, CacheDuration.BlogClientResult),
            buildLocalMemoryCache(DefaultNaverBlogSearchClientWrapperFactory.NaverClientSearchCacheName, CacheDuration.BlogClientResult),
            buildLocalMemoryCache("CLIENT:LOCAL", 5),
        )
        cacheManager.setCaches(caches)
        cacheManager.initializeCaches()

        return cacheManager
    }

    private fun buildLocalMemoryCache(cacheName: String, second: Long): Cache {
        return CaffeineCache(
            cacheName, Caffeine.newBuilder()
                .expireAfterWrite(second, TimeUnit.SECONDS)
                .build()
        )
    }

    override fun cacheResolver(): CacheResolver? {
        return null
    }

    override fun errorHandler(): CacheErrorHandler {
        return object : CacheErrorHandler {
            private var log = KotlinLogging.logger { }

            override fun handleCacheGetError(exception: RuntimeException, cache: Cache, key: Any) {
                val rootCause = ExceptionUtils.getRootCause(exception)
                log.warn("[CACHE ERROR] - 캐시 GET실패. 해당 캐시 제거 : $key", rootCause)
                cache.evict(key)
            }

            override fun handleCachePutError(exception: RuntimeException, cache: Cache, key: Any, value: Any?) {
                val rootCause = ExceptionUtils.getRootCause(exception)
                log.warn("[CACHE ERROR] - 캐시 PUT실패", rootCause)
            }

            override fun handleCacheEvictError(exception: RuntimeException, cache: Cache, key: Any) {
                val rootCause = ExceptionUtils.getRootCause(exception)
                log.warn("[CACHE ERROR] - 캐시 EVICT실패", rootCause)
            }

            override fun handleCacheClearError(exception: RuntimeException, cache: Cache) {
                val rootCause = ExceptionUtils.getRootCause(exception)
                log.warn("[CACHE ERROR] - 캐시 CLEAR실패", rootCause)
            }
        }
    }

}
