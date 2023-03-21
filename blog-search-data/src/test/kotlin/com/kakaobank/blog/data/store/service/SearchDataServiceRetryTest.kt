package com.kakaobank.blog.data.store.service

import com.kakaobank.blog.data.extractor.KeywordExtractor
import com.kakaobank.blog.data.store.model.RawQueryCount
import com.kakaobank.blog.data.store.param.SearchTermSaveParam
import com.kakaobank.blog.data.store.repo.KeywordCountRepo
import com.kakaobank.blog.data.store.repo.RawQueryCountRepo
import com.kakaobank.blog.data.store.repo.SearchHistoryRepo
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.mockito.ArgumentCaptor
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.then
import org.mockito.kotlin.any
import org.mockito.kotlin.times
import org.mockito.kotlin.willReturn
import org.mockito.kotlin.willThrow
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.dao.CannotAcquireLockException
import org.springframework.retry.annotation.EnableRetry
import java.time.LocalDateTime

@SpringBootTest
@EnableRetry
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SearchDataServiceRetryTest {
    @Autowired
    lateinit var service: SearchDataService

    @MockBean
    lateinit var keywordExtractor: KeywordExtractor

    @MockBean
    lateinit var searchHistoryRepo: SearchHistoryRepo

    @MockBean
    lateinit var keywordCountRepo: KeywordCountRepo

    @MockBean
    lateinit var rawQueryCountRepo: RawQueryCountRepo

    @Test
    fun `데드락이 발생했을 경우 재시도하여야 한다`() {
        //given
        val searchDateTime = LocalDateTime.of(2023, 3, 21, 12, 0)
        val text = "blog is 블로그"
        val param = SearchTermSaveParam(text, searchDateTime)

        given(keywordExtractor.canExtract(text)).willReturn(false)
        given(rawQueryCountRepo.findByTextWithLock(any()))
            .willThrow { CannotAcquireLockException("데드락!") }
            .willReturn { null }


        //when
        val result = service.saveSearchTerm(param)

        //then
        then(rawQueryCountRepo).should(times(2)).findByTextWithLock(any())
        ArgumentCaptor.forClass(RawQueryCount::class.java).also { captor ->
            then(rawQueryCountRepo).should(times(1)).save(captor.capture())
            var value = captor.value
            Assertions.assertEquals(text, value.text)
        }


    }


}