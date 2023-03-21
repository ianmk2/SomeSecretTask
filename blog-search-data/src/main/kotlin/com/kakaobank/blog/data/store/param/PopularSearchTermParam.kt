package com.kakaobank.blog.data.store.param

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min

data class PopularSearchTermParam(

    /**
     * 검색할 인기검색어의 수
     */
    @field:Min(1, message = "size는 1~10사이의 정수여야 합니다")
    @field:Max(10, message = "size는 1~10사이의 정수여야 합니다")
    var size: Int = 10,

    /**
     * 인기 검색어의 타입
     */
    var type: PopularSearchTermType = PopularSearchTermType.Raw,
)

enum class PopularSearchTermType {
    /**
     * 검색어 원문
     */
    Raw,

    /**
     * 검색어를 분석하여 토큰단위로 저장한 것
     */
    Keyword,

}