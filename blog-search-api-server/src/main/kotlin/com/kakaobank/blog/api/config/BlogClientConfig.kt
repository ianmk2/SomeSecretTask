package com.kakaobank.blog.api.config

import com.kakaobank.blog.client.BlogClientConfiguration
import com.kakaobank.blog.client.external.kakao.KakaoAPIClientProperties
import com.kakaobank.blog.client.external.naver.NaverAPIClientProperties
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import


@Configuration
class BlogClientKeyConfig {

    @Bean
    fun kakao(
        @Value("\${app.blog.client.key.kakao.api-key}") apiKey: String,
    ): KakaoAPIClientProperties {
        return KakaoAPIClientProperties(apiKey)
    }

    @Bean
    fun naver(
        @Value("\${app.blog.client.key.naver.client-id}") clientId: String,
        @Value("\${app.blog.client.key.naver.client-secret}") clientSecret: String,
    ): NaverAPIClientProperties {
        return NaverAPIClientProperties(
            clientId = clientId,
            clientSecret = clientSecret,
        )
    }
}
