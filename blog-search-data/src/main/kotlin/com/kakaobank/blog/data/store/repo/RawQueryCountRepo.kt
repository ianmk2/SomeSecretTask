package com.kakaobank.blog.data.store.repo

import com.kakaobank.blog.data.store.model.QRawQueryCount
import com.kakaobank.blog.data.store.model.RawQueryCount
import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport

interface RawQueryCountRepo : JpaRepository<RawQueryCount, Long>, RawQueryCountRepoCustom {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("FROM RawQueryCount WHERE text = :text")
    fun findByTextWithLock(text: String): RawQueryCount?
}

interface RawQueryCountRepoCustom {
    fun findAllByPopularKeyword(size: Int): List<RawQueryCount>
}

class RawQueryCountRepoImpl : RawQueryCountRepoCustom, QuerydslRepositorySupport(RawQueryCount::class.java) {
    override fun findAllByPopularKeyword(size: Int): List<RawQueryCount> {
        return from(QRawQueryCount.rawQueryCount)
            .orderBy(QRawQueryCount.rawQueryCount.count.desc())
            .limit(size.toLong())
            .fetch()

    }
}