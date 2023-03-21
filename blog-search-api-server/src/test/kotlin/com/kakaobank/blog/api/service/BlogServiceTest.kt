package com.kakaobank.blog.api.service

import com.kakaobank.blog.api.model.param.BlogSearchAPIParam
import com.kakaobank.blog.api.model.param.RecentSearchAPIParam
import com.kakaobank.blog.client.BlogSearchClientChain
import com.kakaobank.blog.client.model.*
import com.kakaobank.blog.data.store.dto.SimplePage
import com.kakaobank.blog.data.store.param.SearchTermSaveParam
import com.kakaobank.blog.data.store.service.SearchDataQueryService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentCaptor
import org.mockito.BDDMockito.given
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.then
import org.springframework.context.ApplicationEventPublisher

@ExtendWith(MockitoExtension::class)
class BlogServiceTest {
    @InjectMocks
    lateinit var service: BlogService

    @Mock
    lateinit var blogSearchClient: BlogSearchClientChain

    @Mock
    lateinit var searchDataQueryService: SearchDataQueryService

    @Mock
    lateinit var eventPublisher: ApplicationEventPublisher


    @Test
    fun `검색시 저장을 위한 비동기 이벤트 발생 및 API요청이 이뤄져야 한다`() {

        //given
        val param = BlogSearchAPIParam("Test", page = 2, size = 3, sort = BlogSearchSort.Accuracy, source = "Kakao")
        val response = BlogSearchResponse(
            listOf(BlogSearchItem("캐싱된 글", "내용", "https://kakaobank.com", "카카오뱅크", null, "2023-03-21T00:21:22")),
            BlogSearchPagination(1, true)
        )

        given(blogSearchClient.request(param.toBlogSearchParam(), true, "Kakao")).willReturn(
            "Kakao" to response
        )

        //when
        val result = service.searchBlog(param)

        //then
        ArgumentCaptor.forClass(SearchTermSaveParam::class.java).also { captor ->
            then(eventPublisher).should().publishEvent(captor.capture())
            val value = captor.value
            assertEquals("Test", value.text)
        }

        then(blogSearchClient).should().request(any(), any(), any())

        assertEquals(1, result.items.size)
        assertEquals(true, result.pagination.isEnd)
        assertEquals(1, result.pagination.totalCount)
        assertEquals("Kakao", result.source)
    }

    @Test
    fun `getRecentSearch`() {

        //given
        val param = RecentSearchAPIParam(2, 3)

        given(searchDataQueryService.getSearchHistories(param.page, param.size)).willReturn(SimplePage(emptyList(), 1, true))

        //when
        val result = service.getRecentSearch(param)

        //then
        then(searchDataQueryService).should().getSearchHistories(param.page, param.size)
    }


}