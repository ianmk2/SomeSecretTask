package com.kakaobank.blog.data.store.dto

data class SearchTermCount(
    /**
     * 검색어
     */
    val text: String,

    /**
     * 해당 검색어의 검색 횟수
     */
    val count: Long,
)