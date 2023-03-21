package com.kakaobank.blog.data.store.service

import com.kakaobank.blog.data.VALID_QUERY_LENGTH
import com.kakaobank.blog.data.extractor.KeywordExtractor
import com.kakaobank.blog.data.store.model.KeywordCount
import com.kakaobank.blog.data.store.model.RawQueryCount
import com.kakaobank.blog.data.store.model.SearchHistory
import com.kakaobank.blog.data.store.param.SearchTermSaveParam
import com.kakaobank.blog.data.store.repo.KeywordCountRepo
import com.kakaobank.blog.data.store.repo.RawQueryCountRepo
import com.kakaobank.blog.data.store.repo.SearchHistoryRepo
import org.springframework.dao.CannotAcquireLockException
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.retry.annotation.Backoff
import org.springframework.retry.annotation.Retryable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime


@Service
class SearchDataService(
    private val keywordExtractor: KeywordExtractor,
    private val searchHistoryRepo: SearchHistoryRepo,
    private val keywordCountRepo: KeywordCountRepo,
    private val rawQueryCountRepo: RawQueryCountRepo,
) {


    @Transactional
    @Retryable(
        include = [CannotAcquireLockException::class, DataIntegrityViolationException::class],
        maxAttempts = 5,
        backoff = Backoff(delay = 100, maxDelay = 1000, multiplier = 1.5, random = true)
    )
    fun saveSearchTerm(param: SearchTermSaveParam): Boolean {
        val trimmedText = param.text.trim().take(VALID_QUERY_LENGTH)
        if (trimmedText.isBlank())
            return false


        //검색자체에 대한 기록
        saveSearchHistory(trimmedText, param.dateTime)

        //검색어에 대한 카운팅 증가
        saveRawQueryCount(trimmedText)

        //검색어에서 키워드를 추출하여 카운팅 증가
        if (keywordExtractor.canExtract(trimmedText)) {
            val extractionResult = keywordExtractor.extract(trimmedText)
            extractionResult.keywords.forEach {
                saveKeywordCount(it)
            }
        }

        return true
    }


    private fun saveSearchHistory(text:String, dateTime:LocalDateTime) {
        searchHistoryRepo.save(SearchHistory(text = text, createdAt = dateTime))
    }

    private fun saveRawQueryCount(text: String) {
        val rawQueryCount = rawQueryCountRepo.findByTextWithLock(text) ?: RawQueryCount(text = text)
        rawQueryCount.increaseCount()
        rawQueryCountRepo.save(rawQueryCount)
    }

    private fun saveKeywordCount(text: String) {
        val keyword = keywordCountRepo.findByTextWithLock(text) ?: KeywordCount(text = text)
        keyword.increaseCount()
        keywordCountRepo.save(keyword)
    }

}

