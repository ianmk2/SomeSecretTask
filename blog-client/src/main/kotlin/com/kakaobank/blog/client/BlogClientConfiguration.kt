package com.kakaobank.blog.client

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration


/**
 * 외부에서 이 모듈을 이용하고자 할 경우 다음의 코드를 선언함으로써 사용할 수 있다.
 *
 * &#064;Import(BlogClientConfiguration::class)
 */
@Configuration
@EnableFeignClients
@ComponentScan(basePackageClasses = [BlogClientConfiguration::class])
class BlogClientConfiguration {

    /**
     * 각 BlogSearchClientWrapper Bean의 @Order값에 의해 우선순위가 정렬되어 chain client가 만들어진다.
     */
    @Bean
    @Autowired(required = false)
    fun blogClientChain(
        clients: List<BlogSearchClientWrapper<*, *>>?
    ): BlogSearchClientChain {
        return BlogSearchClientChain(clients ?: emptyList())
    }
}