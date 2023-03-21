package com.kakaobank.blog.client.model

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank

data class BlogSearchParam(
    /**
     * 검색어
     */
    @field:NotBlank(message = "검색어는 공백일 수 없습니다")
    val query: String = "",

    /**
     * 결과 문서 정렬 방식
     */
    val sort: BlogSearchSort = BlogSearchSort.Accuracy,

    /**
     * 1-base 페이지 번호
     */
    @field:Min(1, message = "page는 반드시 1 이상이어야 합니다")
    var page: Int = 1,

    /**
     * 한 페이지에서의 볼르그 글의 수
     */
    @field:Min(1, message = "size는 반드시 1 이상이어야 합니다")
    var size: Int = 10,
)

enum class BlogSearchSort {
    /**
     * 정확도순
     */
    Accuracy,

    /**
     * 최신순
     */
    Recency
}

