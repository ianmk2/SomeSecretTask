package com.kakaobank.blog.data.store.repo

import com.kakaobank.blog.data.store.model.KeywordCount
import com.kakaobank.blog.data.store.model.QKeywordCount
import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport

interface KeywordCountRepo : JpaRepository<KeywordCount, Long>, KeywordCountRepoCustom {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("FROM KeywordCount WHERE text = :text")
    fun findByTextWithLock(text: String): KeywordCount?
}

interface KeywordCountRepoCustom {
    fun findAllByPopularKeyword(size: Int): List<KeywordCount>
}

class KeywordCountRepoImpl : KeywordCountRepoCustom, QuerydslRepositorySupport(KeywordCount::class.java) {
    override fun findAllByPopularKeyword(size: Int): List<KeywordCount> {
        return from(QKeywordCount.keywordCount)
            .orderBy(QKeywordCount.keywordCount.count.desc())
            .limit(size.toLong())
            .fetch()
    }
}
