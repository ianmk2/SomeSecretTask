package com.kakaobank.blog.client.external.naver.model

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank

/**
 * @see <a href="https://developers.naver.com/docs/serviceapi/search/blog/blog.md#%ED%8C%8C%EB%9D%BC%EB%AF%B8%ED%84%B0">Request parameter</a>
 */
data class NaverBlogSearchParam(
    /**
     * 검색어
     */
    @field:NotBlank
    var query: String,

    /**
     * 검색 결과 정렬 방법
     */
    var sort: NaverBlogSearchSort? = null,

    /**
     * 검색 시작 위치(기본값: 1, 최댓값: 1000)
     */
    @field:Min(1)
    @field:Max(1000)
    var start: Int? = null,

    /**
     * 한 번에 표시할 검색 결과 개수(기본값: 10, 최댓값: 100)
     */
    @field:Min(1)
    @field:Max(100)
    var display: Int? = null,
)

/**
 * 결과 문서 정렬 방식
 * 기본값 : accuracy
 */
enum class NaverBlogSearchSort {
    /**
     * 유사도순
     */
    sim,

    /**
     * 날짜순
     */
    date,
    ;
}
