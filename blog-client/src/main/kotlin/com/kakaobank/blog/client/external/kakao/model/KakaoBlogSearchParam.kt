package com.kakaobank.blog.client.external.kakao.model

import com.kakaobank.blog.client.*
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank

/**
 * @see <a href="https://developers.kakao.com/docs/latest/ko/daum-search/dev-guide#search-blog-request">Request parameter</a>
 */
data class KakaoBlogSearchParam(
    /**
     * 검색을 원하는 질의어
     * 특정 블로그 글만 검색하고 싶은 경우, 블로그 url과 검색어를 공백(' ') 구분자로 넣을 수 있음
     */
    @field:NotBlank
    var query: String,

    /**
     * 결과 문서 정렬 방식
     */
    var sort: KakaoBlogSearchSort? = null,

    /**
     * 1-base 페이지 번호
     */
    @field:Min(1)
    @field:Max(50)
    var page: Int? = null,

    /**
     * 한페이지에서 보여질 문서 수
     */
    @field:Min(1)
    @field:Max(50)
    var size: Int? = null,
)


/**
 * 결과 문서 정렬 방식
 * 기본값 : accuracy
 */
enum class KakaoBlogSearchSort {
    /**
     * 정확도순
     */
    accuracy,

    /**
     * 최신순
     */
    recency,
    ;
}


