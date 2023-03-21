package com.kakaobank.blog.client.external.naver.model

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDate

/**
 * @see <a href="https://developers.naver.com/docs/serviceapi/search/blog/blog.md#%EC%9D%91%EB%8B%B5">응답</a>
 */
data class NaverBlogSearchResponse(

    /**
     * 검색 결과를 생성한 시간
     */
    var lastBuildDate: String,

    /**
     * 총 검색 결과 개수
     */
    var total: Int,

    /**
     * 검색 시작 위치
     */
    var start: Int,

    /**
     * 한 번에 표시할 검색 결과 개수
     */
    var display: Int,

    /**
     * 개별 검색 결과
     */
    var items: List<NaverBlogSearchItem>,
) {
    val isEnd: Boolean
        get() = start >= 1000 || total < display + start
}


/**
 * @see <a href="https://developers.naver.com/docs/serviceapi/search/blog/blog.md#%EC%9D%91%EB%8B%B5">응답</a>
 */
data class NaverBlogSearchItem(
    /**
     * 블로그 포스트의 제목. 제목에서 검색어와 일치하는 부분은 <b> 태그로 감싸져 있습니다.
     */
    var title: String,

    /**
     * 블로그 포스트의 내용을 요약한 패시지 정보. 패시지 정보에서 검색어와 일치하는 부분은 <b> 태그로 감싸져 있습니다.
     */
    var description: String,

    /**
     * 블로그 포스트의 URL
     */
    var link: String,

    /**
     * 블로그 포스트가 있는 블로그의 이름
     */
    @field:JsonProperty("bloggername")
    var bloggerName: String,

    /**
     * 블로그 포스트가 있는 블로그의 주소
     */
    @field:JsonProperty("bloggerlink")
    var bloggerLink: String,

    /**
     * 블로그 포스트가 작성된 날짜
     */
    @field:JsonFormat(pattern = "yyyyMMdd")
    var postdate: LocalDate,
)
