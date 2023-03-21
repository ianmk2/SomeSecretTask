package com.kakaobank.blog.client.external.kakao.model

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.OffsetDateTime

/**
 * @see <a href="https://developers.kakao.com/docs/latest/ko/daum-search/dev-guide#search-blog-response">Response</a>
 */
data class KakaoBlogSearchResponse(
    var documents: List<KakaoBlogSearchDocument>,
    var meta: KakaoBlogSearchMeta,
)

/**
 * @see <a href="https://developers.kakao.com/docs/latest/ko/daum-search/dev-guide#search-blog-documents">Response Document</a>
 */
data class KakaoBlogSearchDocument(
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
    @field:JsonProperty("blogname")
    var blogName: String,

    /**
     * 검색 시스템에서 추출한 대표 미리보기 이미지 URL, 미리보기 크기 및 화질은 변경될 수 있음
     */
    var thumbnail: String,

    /**
     * 블로그 글 작성시간, ISO 8601
     */
    var datetime: OffsetDateTime,
)

/**
 * @see <a href="https://developers.kakao.com/docs/latest/ko/daum-search/dev-guide#search-blog-response-meta">Response Meta</a>
 */
data class KakaoBlogSearchMeta(
    /**
     * 검색된 문서 수
     */
    @field:JsonProperty("total_count")
    var totalCount: Int,

    /**
     * totalCount 중 노출 가능 문서 수
     */
    @field:JsonProperty("pageable_count")
    var pageableCount: Int,

    /**
     * 현재 페이지가 마지막 페이지인지 여부, 값이 false면 page를 증가시켜 다음 페이지를 요청할 수 있음
     */
    @field:JsonProperty("is_end")
    var isEnd: Boolean,
)
