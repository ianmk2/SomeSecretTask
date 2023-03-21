package com.kakaobank.blog.client.external.kakao

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.kakaobank.blog.client.*
import com.kakaobank.blog.client.error.BlogAPIClientErrorCode
import com.kakaobank.blog.client.error.BlogAPIClientException
import com.kakaobank.blog.client.external.kakao.model.KakaoBlogSearchParam
import com.kakaobank.blog.client.external.kakao.model.KakaoBlogSearchResponse
import com.kakaobank.blog.client.external.kakao.model.KakaoBlogSearchSort
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
@ConditionalOnMissingBean(KakaoBlogSearchClientWrapper::class)
@ConditionalOnBean(KakaoAPIClientProperties::class)
class DefaultKakaoBlogSearchClientWrapperFactory {
    companion object {
        const val KakaoClientSearchCacheName = "CLIENT:KAKAO"
    }

    @Bean
    @Order(0)
    @Autowired(required = false)
    fun kakaoBlogSearchClientWrapper(
        client: KakaoAPIClient,
        objectMapper: ObjectMapper,
        cacheManager: CacheManager?,
    ): KakaoBlogSearchClientWrapper {
        return KakaoBlogSearchClientWrapper(client, objectMapper, cacheManager?.getCache(KakaoClientSearchCacheName))
    }
}


class KakaoBlogSearchClientWrapper(
    private val client: KakaoAPIClient,
    private val objectMapper: ObjectMapper,
    private val cache: Cache?,
) : BlogSearchClientWrapper<KakaoBlogSearchParam, KakaoBlogSearchResponse>() {

    override val sourceName: String = "Kakao"
    override fun getCache(): Cache? = cache

    override fun request(param: KakaoBlogSearchParam): KakaoBlogSearchResponse {
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

    override fun translateParameter(param: BlogSearchParam): KakaoBlogSearchParam {
        return KakaoBlogSearchParam(
            query = param.query,
            sort = when (param.sort) {
                BlogSearchSort.Accuracy -> KakaoBlogSearchSort.accuracy
                BlogSearchSort.Recency -> KakaoBlogSearchSort.recency
            },
            page = param.page,
            size = param.size,
        )
    }


    override fun translateResponse(response: KakaoBlogSearchResponse): BlogSearchResponse {

        val items = response.documents.map {
            BlogSearchItem(
                title = it.title,
                contents = it.contents,
                url = it.url,
                blogName = it.blogName,
                thumbnail = it.thumbnail,
                postedAt = it.datetime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME),
            )
        }

        val pagination = BlogSearchPagination(
            //조회가능한 아이템의 수라는 면에서 totalCount가 아닌, pageableCount로 접근.
            //그러나, 검색하는 page 마다 pageableCount가 변동되는 이슈가 발생함(API의 문제로 추정)
            totalCount = response.meta.pageableCount,
            isEnd = response.meta.isEnd,
        )
        return BlogSearchResponse(items, pagination)
    }


}