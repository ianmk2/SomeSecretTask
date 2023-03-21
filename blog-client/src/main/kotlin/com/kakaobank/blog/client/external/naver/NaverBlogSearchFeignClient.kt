package com.kakaobank.blog.client.external.naver

import com.kakaobank.blog.client.error.BlogAPIClientErrorCode
import com.kakaobank.blog.client.error.BlogAPIClientException
import com.kakaobank.blog.client.external.DefaultFeignFallbackFactory
import com.kakaobank.blog.client.external.naver.model.NaverBlogSearchParam
import feign.RequestInterceptor
import feign.Response
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.cloud.openfeign.SpringQueryMap
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.GetMapping

@FeignClient(
    name = "naverBlogSearchAPIFeignClient",
    url = "https://openapi.naver.com",
    dismiss404 = false,
    configuration = [NaverAPIClientConfig::class],
    fallbackFactory = NaverAPIClientFallbackFactory::class,
)
interface NaverAPIClient {
    @GetMapping("/v1/search/blog.json")
    fun searchBlog(@SpringQueryMap param: NaverBlogSearchParam): Response
}


@Component
class NaverAPIClientFallbackFactory : DefaultFeignFallbackFactory<NaverAPIClient>()


class NaverAPIClientConfig(
    private var properties: NaverAPIClientProperties?,
) {
    @Bean
    fun authorization(): RequestInterceptor {
        return RequestInterceptor {
            it.header(
                "X-Naver-Client-Id", properties?.clientId
                    ?: throw BlogAPIClientException(BlogAPIClientErrorCode.InvalidAPIKey)
            )
            it.header(
                "X-Naver-Client-Secret", properties?.clientSecret
                    ?: throw BlogAPIClientException(BlogAPIClientErrorCode.InvalidAPIKey)
            )
        }
    }
}