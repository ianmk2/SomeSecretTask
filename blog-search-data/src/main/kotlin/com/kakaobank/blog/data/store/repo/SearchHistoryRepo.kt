package com.kakaobank.blog.data.store.repo

import com.kakaobank.blog.data.store.model.SearchHistory
import org.springframework.data.jpa.repository.JpaRepository

interface SearchHistoryRepo : JpaRepository<SearchHistory, Long>
