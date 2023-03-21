package com.kakaobank.blog.data.store.service

import com.kakaobank.blog.data.extractor.KeywordExtractionResult
import com.kakaobank.blog.data.extractor.KeywordExtractor
import com.kakaobank.blog.data.store.model.KeywordCount
import com.kakaobank.blog.data.store.model.RawQueryCount
import com.kakaobank.blog.data.store.model.SearchHistory
import com.kakaobank.blog.data.store.param.SearchTermSaveParam
import com.kakaobank.blog.data.store.repo.KeywordCountRepo
import com.kakaobank.blog.data.store.repo.RawQueryCountRepo
import com.kakaobank.blog.data.store.repo.SearchHistoryRepo
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentCaptor
import org.mockito.BDDMockito.*
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import java.time.LocalDateTime

@ExtendWith(MockitoExtension::class)
class SearchDataServiceTest {
    @InjectMocks
    lateinit var service: SearchDataService

    @Mock
    lateinit var keywordExtractor: KeywordExtractor

    @Mock
    lateinit var searchHistoryRepo: SearchHistoryRepo

    @Mock
    lateinit var keywordCountRepo: KeywordCountRepo

    @Mock
    lateinit var rawQueryCountRepo: RawQueryCountRepo

    @Test
    fun `빈 공백의 문자열은 저장되지 않아야 한다`() {
        //given
        val searchDateTime = LocalDateTime.of(2023, 3, 21, 12, 0)
        val text = " "
        val param = SearchTermSaveParam(text, searchDateTime)

        //when
        val result = service.saveSearchTerm(param)

        //then
        Assertions.assertEquals(false, result)
        then(keywordExtractor).shouldHaveNoInteractions()
        then(searchHistoryRepo).shouldHaveNoInteractions()
        then(keywordCountRepo).shouldHaveNoInteractions()
        then(rawQueryCountRepo).shouldHaveNoInteractions()

    }

    @Test
    fun `매우 긴 문자열은 30자로 잘려서 저장되어야 한다`() {
        //given
        val searchDateTime = LocalDateTime.of(2023, 3, 21, 12, 0)
        val text = "LOOOOOOOOOOOOOOOOOOOOOOOOOOONG__"
        val shortText = "LOOOOOOOOOOOOOOOOOOOOOOOOOOONG"
        val param = SearchTermSaveParam(text, searchDateTime)


        //when
        val result = service.saveSearchTerm(param)

        //then

        ArgumentCaptor.forClass(SearchHistory::class.java).also { captor ->
            then(searchHistoryRepo).should().save(captor.capture())
            val value = captor.value
            assertEquals(shortText, value.text)
            assertEquals(searchDateTime, value.createdAt)
        }
    }


    @Test
    fun `검색어를 저장한 경우 키워드가 추출되어 기록과 함께 저장되어야 한다 - 기존에 해당키워드의 기록이 없을 때`() {
        //given
        val searchDateTime = LocalDateTime.of(2023, 3, 21, 12, 0)
        val text = "blog is 블로그"
        val param = SearchTermSaveParam(text, searchDateTime)

        given(keywordExtractor.extract(any())).willReturn(
            KeywordExtractionResult(text, setOf("blog", "블로그"))
        )
        given(keywordExtractor.canExtract(text)).willReturn(true)


        //when
        val result = service.saveSearchTerm(param)

        //then
        ArgumentCaptor.forClass(SearchHistory::class.java).also { captor ->
            then(searchHistoryRepo).should().save(captor.capture())
            val value = captor.value
            assertEquals(text, value.text)
            assertEquals(searchDateTime, value.createdAt)
        }

        ArgumentCaptor.forClass(RawQueryCount::class.java).also { captor ->
            then(rawQueryCountRepo).should().save(captor.capture())
            var value = captor.value
            assertEquals(0L, value.id)
            assertEquals(text, value.text)
            assertEquals(1, value.count)
        }

        ArgumentCaptor.forClass(KeywordCount::class.java).also { captor ->
            then(keywordCountRepo).should(times(2)).save(captor.capture())
            var value = captor.allValues[0]
            assertEquals(0L, value.id)
            assertEquals("blog", value.text)
            assertEquals(1, value.count)
            value = captor.allValues[1]
            assertEquals(0L, value.id)
            assertEquals("블로그", value.text)
            assertEquals(1, value.count)
        }

    }

    @Test
    fun `검색어를 저장한 경우 키워드가 추출되어 기록과 함께 저장되어야 한다 - 기존에 해당키워드의 기록이 있을 때`() {
        //given
        val searchDateTime = LocalDateTime.of(2023, 3, 21, 12, 0)
        val text = "blog is 블로그"
        val param = SearchTermSaveParam(text, searchDateTime)

        given(keywordExtractor.extract(any())).willReturn(
            KeywordExtractionResult(text, setOf("blog", "블로그"))
        )
        given(keywordExtractor.canExtract(text)).willReturn(true)
        given(rawQueryCountRepo.findByTextWithLock(any())).willAnswer { RawQueryCount(text = it.arguments[0] as String, count = 2) }
        given(keywordCountRepo.findByTextWithLock(any())).willAnswer { KeywordCount(text = it.arguments[0] as String, count = 1) }


        //when
        val result = service.saveSearchTerm(param)

        //then
        ArgumentCaptor.forClass(SearchHistory::class.java).also { captor ->
            then(searchHistoryRepo).should().save(captor.capture())
            val value = captor.value
            assertEquals(text, value.text)
            assertEquals(searchDateTime, value.createdAt)
        }

        ArgumentCaptor.forClass(RawQueryCount::class.java).also { captor ->
            then(rawQueryCountRepo).should().save(captor.capture())
            var value = captor.value
            assertEquals(text, value.text)
            assertEquals(3, value.count)
        }

        ArgumentCaptor.forClass(KeywordCount::class.java).also { captor ->
            then(keywordCountRepo).should(times(2)).save(captor.capture())
            var value = captor.allValues[0]

            assertEquals("blog", value.text)
            assertEquals(2, value.count)
            value = captor.allValues[1]

            assertEquals("블로그", value.text)
            assertEquals(2, value.count)
        }

    }


}

