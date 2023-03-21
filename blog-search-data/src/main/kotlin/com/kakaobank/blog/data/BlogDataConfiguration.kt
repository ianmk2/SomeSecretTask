package com.kakaobank.blog.data

import com.kakaobank.blog.data.extractor.KeywordExtractor
import com.kakaobank.blog.data.extractor.KeywordExtractorChain
import com.kakaobank.blog.data.extractor.impl.EnglishKeywordExtractor
import com.kakaobank.blog.data.extractor.impl.KoreanKeywordExtractor
import org.springframework.boot.SpringBootConfiguration
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.retry.annotation.EnableRetry



@EnableRetry
@EnableJpaAuditing
@EnableJpaRepositories
@Configuration
class DataEnableConfig

@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan(basePackageClasses = [BlogDataConfiguration::class])
@EntityScan(basePackageClasses = [BlogDataConfiguration::class])
class BlogDataConfiguration {

    /**
     * 기본 키워드 추출기 체인
     * 한글과 영어를 포함함
     */
    @Bean
    @ConditionalOnMissingBean(KeywordExtractor::class)
    fun keywordExtractor(): KeywordExtractor {
        return KeywordExtractorChain(
            extractors = listOf(
                KoreanKeywordExtractor(),
                EnglishKeywordExtractor(),
            )
        )
    }

}