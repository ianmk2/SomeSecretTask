package com.kakaobank.blog.client.external.kakao

import com.kakaobank.blog.client.error.BlogAPIClientErrorCode
import com.kakaobank.blog.client.error.BlogAPIClientException
import com.kakaobank.blog.client.external.DefaultFeignFallbackFactory
import com.kakaobank.blog.client.external.kakao.model.KakaoBlogSearchParam
import feign.RequestInterceptor
import feign.Response
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.cloud.openfeign.SpringQueryMap
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.GetMapping

@FeignClient(
    name = "kakaoBlogSearchFeignClient",
    url = "https://dapi.kakao.com",
    dismiss404 = false,
    configuration = [KakaoAPIClientConfig::class],
    fallbackFactory = KakaoAPIClientFallbackFactory::class,
)
interface KakaoAPIClient {
    @GetMapping("/v2/search/blog")
    fun searchBlog(@SpringQueryMap param: KakaoBlogSearchParam): Response
}


@Component
class KakaoAPIClientFallbackFactory : DefaultFeignFallbackFactory<KakaoAPIClient>()


class KakaoAPIClientConfig(
    private var properties: KakaoAPIClientProperties?,
) {
    @Bean
    fun authorization(): RequestInterceptor {
        return RequestInterceptor {
            val apiKey = properties?.apiKey ?: throw BlogAPIClientException(BlogAPIClientErrorCode.InvalidAPIKey)

            it.header("Authorization", "KakaoAK $apiKey")
        }
    }
}