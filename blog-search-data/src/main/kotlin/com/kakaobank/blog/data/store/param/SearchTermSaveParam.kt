package com.kakaobank.blog.data.store.param

import java.time.LocalDateTime

class SearchTermSaveParam(
    /**
     * 검색어
     */
    val text: String,

    /**
     * 언제 검색했는지에 대한 시간정보정보
     */
    val dateTime: LocalDateTime,
)