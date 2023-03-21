package com.kakaobank.blog.api.service

import com.kakaobank.blog.api.config.CacheConfig
import com.kakaobank.blog.api.model.param.PopularSearchTermAPIParam
import com.kakaobank.blog.client.BlogSearchClientChain
import com.kakaobank.blog.data.store.dto.SearchTermCount
import com.kakaobank.blog.data.store.param.PopularSearchTermParam
import com.kakaobank.blog.data.store.param.PopularSearchTermType
import com.kakaobank.blog.data.store.service.SearchDataQueryService
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.times
import org.mockito.kotlin.any
import org.mockito.kotlin.then
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.ApplicationEventPublisher

@SpringBootTest(classes = [BlogService::class, CacheConfig::class])
@AutoConfigureCache
@EnableCaching
class BlogServiceIntegrationTest {
    @Autowired
    lateinit var service: BlogService

    @MockBean
    lateinit var searchDataQueryService: SearchDataQueryService


    @MockBean
    lateinit var blogSearchClient: BlogSearchClientChain

    @MockBean
    lateinit var eventPublisher: ApplicationEventPublisher


    @Test
    fun `같은 타입 및 사이즈에 대한 인기 검색어는 10초 동안 캐싱되어야 한다`() {

        //given
        val param = PopularSearchTermParam(2, PopularSearchTermType.Raw)
        given(searchDataQueryService.getPopularSearchTerm(param)).willReturn(
            listOf(
                SearchTermCount("A", 2),
                SearchTermCount("B", 1),
            )
        )

        //when
        val result1 = service.getPopular(PopularSearchTermAPIParam())
        val result2 = service.getPopular(PopularSearchTermAPIParam())
        Thread.sleep(10_000)
        val result3 = service.getPopular(PopularSearchTermAPIParam())

        //then
        then(searchDataQueryService).should(times(2)).getPopularSearchTerm(any())
    }

    @Test
    fun `다른 타입 및 사이즈에 대한 인기 검색어는 별도로 요청되어야 한다`() {

        //given
        val param = PopularSearchTermParam(2, PopularSearchTermType.Raw)
        given(searchDataQueryService.getPopularSearchTerm(param)).willReturn(
            listOf(
                SearchTermCount("A", 2),
                SearchTermCount("B", 1),
            )
        )

        //when
        val result1 = service.getPopular(PopularSearchTermAPIParam(1))
        val result2 = service.getPopular(PopularSearchTermAPIParam(2))

        //then
        then(searchDataQueryService).should(times(2)).getPopularSearchTerm(any())
    }


}