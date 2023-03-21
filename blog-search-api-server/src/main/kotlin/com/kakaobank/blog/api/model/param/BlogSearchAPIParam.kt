package com.kakaobank.blog.api.model.param

import com.kakaobank.blog.client.model.BlogSearchParam
import com.kakaobank.blog.client.model.BlogSearchSort
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank


@Schema(description = "블로그 검색을 위한 파라미터")
data class BlogSearchAPIParam(

    @field:Schema(description = "검색어")
    @field:NotBlank(message = "검색어는 공백일 수 없습니다")
    var query: String = "",

    @field:Schema(description = "정렬방식(기본값: Accuracy). Accuracy(정확도순, 기본값), Recencay(최신순)", allowableValues=["Accuracy", "Recency"])
    var sort: BlogSearchSort = BlogSearchSort.Accuracy,

    @field:Schema(description = "페이지번호. 1~50사이의 정수(기본값1)")
    @field:Min(1, message = "page는 반드시 1 이상이어야 합니다")
    var page: Int = 1,

    @field:Schema(description = "한 페이지에서 보여질 아이템의 수. 1~50사이의 정수. (기본값10)")
    @field:Min(1, message = "size는 반드시 1 이상이어야 합니다")
    var size: Int = 10,

    @field:Schema(description = "블로그를 어디에서 검색할 것인지에 대한 지정. 미지정시 카카오 -> 네이버 순으로 장애시 다음 소스를 검색하게 됨", allowableValues = ["Kakao", "Naver"])
    var source:String? = null,
) {

    fun toBlogSearchParam(): BlogSearchParam {
        return BlogSearchParam(query, sort, page, size)
    }
}



