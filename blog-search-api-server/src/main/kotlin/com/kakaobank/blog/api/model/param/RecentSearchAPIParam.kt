package com.kakaobank.blog.api.model.param

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Min

@Schema(description = "최근 검색어 조회를 위한 파라미터")
data class RecentSearchAPIParam(

    @field:Schema(description = "페이지번호. (기본값1)")
    @field:Min(1, message = "page는 반드시 1 이상이어야 합니다")
    var page: Int = 1,

    @field:Schema(description = "한 페이지에서 보여질 아이템의 수 (기본값10)")
    @field:Min(1, message = "size는 반드시 1 이상이어야 합니다")
    var size: Int = 10,
)