package com.kakaobank.blog.client

import com.kakaobank.blog.client.error.BlogAPIClientErrorCode
import com.kakaobank.blog.client.error.BlogAPIClientException
import com.kakaobank.blog.client.model.BlogSearchParam
import com.kakaobank.blog.client.model.BlogSearchResponse

open class BlogSearchClientChain(
    private val clients: List<BlogSearchClientWrapper<*, *>> = emptyList()
) {

    /**
     * 블로그를 클라이언트 모음을 통해 검색한다
     * @param useCache 해당 클라이언트가 캐시를 사용할 수 있는 경우 캐시를 이용함
     * @return Pair < 클라이언트ID, 검색결과 >
     */
    open fun request(param: BlogSearchParam, useCache: Boolean = false, source: String? = null): Pair<String, BlogSearchResponse> {
        var lastException: BlogAPIClientException? = null
        var foundClient = false
        for (client in clients) {
            if (source != null && client.sourceName != source) {
                continue
            }
            foundClient = true
            try {
                //클라이언트를 차례로 시도하고 온전히 결과를 얻는 순간 결과를 바로 리턴한다.
                return client.sourceName to client.search(param, useCache)
            } catch (e: BlogAPIClientException) {
                lastException = e
            } catch (e: Exception) {
                throw BlogAPIClientException(BlogAPIClientErrorCode.UnexpectedError, exception = e)
            }
        }
        if (source != null && foundClient && lastException != null) {
            throw lastException
        }
        throw BlogAPIClientException(BlogAPIClientErrorCode.AllBlogServicesDown, exception = lastException)
    }
}