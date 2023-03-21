package com.kakaobank.blog.client.external.naver

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.kakaobank.blog.client.BlogSearchClientWrapper
import com.kakaobank.blog.client.error.BlogAPIClientErrorCode
import com.kakaobank.blog.client.error.BlogAPIClientException
import com.kakaobank.blog.client.external.naver.model.NaverBlogSearchParam
import com.kakaobank.blog.client.external.naver.model.NaverBlogSearchResponse
import com.kakaobank.blog.client.external.naver.model.NaverBlogSearchSort
import com.kakaobank.blog.client.model.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.cache.Cache
import org.springframework.cache.CacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import java.time.format.DateTimeFormatter

@Configuration
@ConditionalOnMissingBean(NaverBlogSearchClientWrapper::class)
@ConditionalOnBean(NaverAPIClientProperties::class)
class DefaultNaverBlogSearchClientWrapperFactory {
    companion object {
        const val NaverClientSearchCacheName = "CLIENT:NAVER"
    }

    @Bean
    @Order(10)
    @Autowired(required = false)
    fun naverBlogSearchClientWrapper(
        client: NaverAPIClient,
        objectMapper: ObjectMapper,
        cacheManager: CacheManager?,
    ): NaverBlogSearchClientWrapper {
        return NaverBlogSearchClientWrapper(client, objectMapper, cacheManager?.getCache(NaverClientSearchCacheName))
    }
}

class NaverBlogSearchClientWrapper(
    private val client: NaverAPIClient,
    private val objectMapper: ObjectMapper,
    private val cache: Cache?,
) : BlogSearchClientWrapper<NaverBlogSearchParam, NaverBlogSearchResponse>() {

    override val sourceName: String = "Naver"
    override fun getCache(): Cache? = cache

    override fun request(param: NaverBlogSearchParam): NaverBlogSearchResponse {
        val response = client.searchBlog(param)

        val (status, body) = getStatusBodyPair(response)
        when (status) {
            400 -> BlogAPIClientErrorCode.InvalidParameter
            429 -> BlogAPIClientErrorCode.ExceedQuota
            500 -> BlogAPIClientErrorCode.ServicesDown
            else -> null
        }?.run {
            throw BlogAPIClientException(this, responseStatusCode = status, responseBody = body)
        }
        try {
            return objectMapper.readValue(body)
        } catch (e: Exception) {
            throw BlogAPIClientException(BlogAPIClientErrorCode.ParseFail, e)
        }
    }

    override fun translateParameter(param: BlogSearchParam): NaverBlogSearchParam {
        return NaverBlogSearchParam(
            query = param.query,
            sort = when (param.sort) {
                BlogSearchSort.Accuracy -> NaverBlogSearchSort.sim
                BlogSearchSort.Recency -> NaverBlogSearchSort.date
            },
            start = (param.page - 1) * param.size + 1,
            display = param.size,
        )
    }

    override fun translateResponse(response: NaverBlogSearchResponse): BlogSearchResponse {

        val items = response.items.map {
            BlogSearchItem(
                title = it.title,
                contents = it.description,
                url = it.link,
                blogName = it.bloggerName,
                thumbnail = null,
                postedAt = it.postdate.format(DateTimeFormatter.ISO_DATE),
            )
        }

        val pagination = BlogSearchPagination(
            totalCount = response.total,
            isEnd = response.isEnd,
        )
        return BlogSearchResponse(items, pagination)
    }


}