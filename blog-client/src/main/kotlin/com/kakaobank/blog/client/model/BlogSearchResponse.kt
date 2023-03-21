package com.kakaobank.blog.client.model

open class BlogSearchResponse(
    var items: List<BlogSearchItem>,
    var pagination: BlogSearchPagination,
)

open class BlogSearchPagination(
    var totalCount: Int,
    var isEnd: Boolean,
)


data class BlogSearchItem(

    /**
     * 블로그 글 제목
     */
    var title: String,

    /**
     * "블로그 글 요약"
     */
    var contents: String,

    /**
     * 블로그 글 URL
     */
    var url: String,

    /**
     * 블로그의 이름
     */
    var blogName: String,

    /**
     * 검색 시스템에서 추출한 대표 미리보기 이미지 URL, 미리보기 크기 및 화질은 변경될 수 있음
     */
    var thumbnail: String? = null,


    /**
     * 블로그 글 작성시간, ISO 8601의 날짜시간 or 날짜
     */
    var postedAt: String,
)