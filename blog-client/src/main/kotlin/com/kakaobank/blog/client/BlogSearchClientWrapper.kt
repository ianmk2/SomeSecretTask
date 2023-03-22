package com.kakaobank.blog.client

import com.kakaobank.blog.client.model.BlogSearchParam
import com.kakaobank.blog.client.model.BlogSearchResponse
import com.kakaobank.blog.client.util.readAsString
import feign.Response
import mu.KotlinLogging
import org.springframework.cache.Cache

/**
 * 각 블로그 서비스 API를 추상화 하여 단일 인터페이스로 사용할 수 있게 한다.
 */
abstract class BlogSearchClientWrapper<PARAM, RESPONSE : Any> {

    private val log = KotlinLogging.logger {  }
    abstract val sourceName: String

    /**
     * 외부 클라이언트를 통해 요청 하는 부분
     */
    abstract fun request(param: PARAM): RESPONSE

    /**
     * 범용 요청 파라미터 BlogSearchParam -> 각 클라이언트에 맞는 파라미터로 변환
     */
    abstract fun translateParameter(param: BlogSearchParam): PARAM

    /**
     * 각 클라이언트의 결과 -> 범용 결과 DTO로 변환
     */
    abstract fun translateResponse(response: RESPONSE): BlogSearchResponse

    open fun getCache(): Cache? = null

    private fun generateCacheKey(param: BlogSearchParam): String {
        return "${param.query}:${param.sort}:${param.page}:${param.size}"
    }

    open fun search(param: BlogSearchParam, useCache: Boolean = false): BlogSearchResponse {
        val cache = getCache()
        var cacheKey: String? = null
        if (useCache && cache != null) {
            cacheKey = generateCacheKey(param)
            val cacheData = cache.get(cacheKey, BlogSearchResponse::class.java)
            if (cacheData != null) {
                log.info { "CACHE HIT! key:$cacheKey" }
                return cacheData
            }
        }
        val translatedParameter = translateParameter(param)
        val rawResponse = request(translatedParameter)
        val translatedResponse = translateResponse(rawResponse)

        if (cacheKey != null) {
            cache?.put(cacheKey, translatedResponse)
        }
        return translatedResponse
    }

    /**
     * FeignResponse로부터 < statusCode , body string >을 추출함
     */
    fun getStatusBodyPair(feignResponse: Response): Pair<Int, String> {
        val statusCode = feignResponse.status()
        val text = feignResponse.body().asInputStream().readAsString()
        return statusCode to text
    }
}


