package com.kakaobank.blog.client

import com.kakaobank.blog.client.error.BlogAPIClientErrorCode
import com.kakaobank.blog.client.error.BlogAPIClientException
import com.kakaobank.blog.client.external.DefaultFeignFallbackFactory
import com.kakaobank.blog.client.external.kakao.KakaoAPIClientConfig
import com.kakaobank.blog.client.model.BlogSearchItem
import com.kakaobank.blog.client.model.BlogSearchPagination
import com.kakaobank.blog.client.model.BlogSearchParam
import com.kakaobank.blog.client.model.BlogSearchResponse
import feign.Response
import mu.KotlinLogging
import org.springframework.cache.Cache
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.GetMapping

/**
 * 로컬로 요청을 하거나, 항상 성공 또는 항상 실패하는 ClientWrapper
 */
open class MockClientWrapper(
    private val client: LocalBlogSearchFeignClient? = null,
    private val cache: Cache? = null,
    private val alwaysFailWithErrorCode: BlogAPIClientErrorCode? = null,
    override val sourceName:String = "MOCK",
) : BlogSearchClientWrapper<String, String>() {
    val log = KotlinLogging.logger { }

    override fun getCache(): Cache? = cache
    override fun request(param: String): String {
        log.info { "MockClientWrapper - request called" }

        if (alwaysFailWithErrorCode != null) {
            throw BlogAPIClientException(alwaysFailWithErrorCode)
        }
        return client?.let { getStatusBodyPair(client.searchBlog()).second } ?: ""
    }

    override fun translateParameter(param: BlogSearchParam): String = ""
    override fun translateResponse(response: String): BlogSearchResponse {
        return BlogSearchResponse(
            listOf(BlogSearchItem("테스트 글", "내용", "https://kakaobank.com", "카카오뱅크", null, "2023-03-21T00:21:22")),
            BlogSearchPagination(1, true)
        )
    }
}

@FeignClient(
    name = "localClient",
    url = "http://localhost:3000",
    dismiss404 = false,
    configuration = [KakaoAPIClientConfig::class],
    fallbackFactory = LocalFeignClientFallbackFactory::class
)
interface LocalBlogSearchFeignClient {
    @GetMapping("/blog")
    fun searchBlog(): Response
}

@Component
class LocalFeignClientFallbackFactory : DefaultFeignFallbackFactory<LocalBlogSearchFeignClient>()


