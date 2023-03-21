package com.kakaobank.blog.data.store.service

import com.kakaobank.blog.data.store.model.KeywordCount
import com.kakaobank.blog.data.store.model.RawQueryCount
import com.kakaobank.blog.data.store.model.SearchHistory
import com.kakaobank.blog.data.store.param.PopularSearchTermParam
import com.kakaobank.blog.data.store.param.PopularSearchTermType
import com.kakaobank.blog.data.store.repo.KeywordCountRepo
import com.kakaobank.blog.data.store.repo.RawQueryCountRepo
import com.kakaobank.blog.data.store.repo.SearchHistoryRepo
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
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import java.time.LocalDateTime

@ExtendWith(MockitoExtension::class)
class SearchDataQueryServiceTest {

    @InjectMocks
    lateinit var service: SearchDataQueryService

    @Mock
    lateinit var keywordCountRepo: KeywordCountRepo

    @Mock
    lateinit var rawQueryCountRepo: RawQueryCountRepo

    @Mock
    lateinit var searchHistoryRepo: SearchHistoryRepo

    @Test
    fun `Keyword 타입으로 요청한 경우, 해당 저장소에서 데이터를 불러와야 한다`() {
        //given
        val param = PopularSearchTermParam(size = 3, PopularSearchTermType.Keyword)
        val items = listOf(
            KeywordCount(text = "Test", count = 1)
        )
        given(keywordCountRepo.findAllByPopularKeyword(param.size)).willReturn(items)

        //when
        val result = service.getPopularSearchTerm(param)

        //then
        assertEquals(1, result.size)
        assertEquals(1, result[0].count)
        assertEquals("Test", result[0].text)

    }


    @Test
    fun `Raw 타입으로 요청한 경우, 해당 저장소에서 데이터를 불러와야 한다`() {
        //given
        val param = PopularSearchTermParam(size = 3, PopularSearchTermType.Raw)
        val items = listOf(
            RawQueryCount(text = "Test", count = 1)
        )
        given(rawQueryCountRepo.findAllByPopularKeyword(param.size)).willReturn(items)

        //when
        val result = service.getPopularSearchTerm(param)

        //then
        assertEquals(1, result.size)
        assertEquals(1, result[0].count)
        assertEquals("Test", result[0].text)
    }

    @Test
    fun `최근 검색어를 조회 할 때 pageable에 맞게 -1를 해서 호출하여야 한다`() {
        //given
        val param = PopularSearchTermParam(size = 3, PopularSearchTermType.Raw)
        val items = listOf(
            RawQueryCount(text = "Test", count = 1)
        )
        given(searchHistoryRepo.findAll(any<Pageable>())).willAnswer {
            PageImpl<SearchHistory>(
                listOf(
                    SearchHistory(2, "2", LocalDateTime.now()),
                    SearchHistory(1, "1", LocalDateTime.now().minusMinutes(1))
                ),
                it.arguments[0] as Pageable,
                4L,
            )
        }

        //when
        val result = service.getSearchHistories(2, 2)

        //then
        ArgumentCaptor.forClass(Pageable::class.java).also {
            then(searchHistoryRepo).should().findAll(it.capture())
            assertEquals(1, it.value.pageNumber)
            assertEquals(2, it.value.pageSize)
        }


        assertEquals(true, result.isEnd)
        assertEquals(4L, result.totalCount)

        assertEquals(2, result.items.size)
        assertEquals(2L, result.items[0].id)
        assertEquals(1L, result.items[1].id)

    }
}
