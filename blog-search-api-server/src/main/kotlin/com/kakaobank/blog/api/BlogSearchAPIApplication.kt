package com.kakaobank.blog.api

import com.kakaobank.blog.client.BlogClientConfiguration
import com.kakaobank.blog.data.BlogDataConfiguration
import mu.KotlinLogging
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableAsync
import java.security.Security
import java.util.*


@EnableCaching
@EnableAsync
@Configuration
class EnableConfig

@SpringBootApplication(
    scanBasePackageClasses = [
        BlogSearchAPIApplication::class,
        BlogDataConfiguration::class,
        BlogClientConfiguration::class,
    ]
)
class BlogSearchAPIApplication

fun main(args: Array<String>) {
    Security.setProperty("networkaddress.cache.ttl", "60")
    Locale.setDefault(Locale.KOREAN)
    TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"))
    val app = runApplication<BlogSearchAPIApplication>(*args)

    val log = KotlinLogging.logger { }
    log.info { "Swagger : http://localhost:8080/swagger-ui.html" }


}


