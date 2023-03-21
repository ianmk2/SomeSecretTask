package com.kakaobank.blog.api.model.dto

import com.kakaobank.blog.client.model.BlogSearchItem
import com.kakaobank.blog.client.model.BlogSearchResponse
import io.swagger.v3.oas.annotations.media.Schema


/**
 * 블로그 검색에 대한 결과
 * BlogAPIClient에서 받은 결과와 구조는 대부분 같으나, 추후 확장성 및 Swagger문서화를 위하여 별도로 분리하였음
 */
@Schema(description = "블로그 검색에 대한 결과")
data class BlogSearchAPIResponseDTO(
    var items: List<BlogDocumentDTO>,
    var pagination: APIPaginationDTO,
    @field:Schema(description = "어떤 서비스에서 검색된 결과인지를 나타냄")
    var source: String,
) {
    constructor(source: String, response: BlogSearchResponse) : this(
        items = response.items.map { BlogDocumentDTO(it) },
        pagination = APIPaginationDTO(response.pagination),
        source = source,
    )
}


data class BlogDocumentDTO(
    @field:Schema(description = "블로그 글 제목")
    var title: String,

    @field:Schema(description = "블로그 글 요약")
    var contents: String,

    @field:Schema(description = "블로그 글 URL")
    var url: String,

    @field:Schema(description = "블로그의 이름")
    var blogName: String,

    @field:Schema(description = "검색 시스템에서 추출한 대표 미리보기 이미지 URL, 미리보기 크기 및 화질은 변경될 수 있음")
    var thumbnail: String? = null,

    @field:Schema(description = "블로그 글 작성시간, ISO 8601의 날짜시간 or 날짜")
    var postedAt: String,
) {
    constructor(blogSearchItem: BlogSearchItem) : this(
        title = blogSearchItem.title,
        contents = blogSearchItem.contents,
        url = blogSearchItem.url,
        blogName = blogSearchItem.blogName,
        thumbnail = blogSearchItem.thumbnail,
        postedAt = blogSearchItem.postedAt,
    )

}

