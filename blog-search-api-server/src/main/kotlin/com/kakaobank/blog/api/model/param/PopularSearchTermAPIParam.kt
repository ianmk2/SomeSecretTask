package com.kakaobank.blog.api.model.param

import com.kakaobank.blog.data.store.param.PopularSearchTermParam
import com.kakaobank.blog.data.store.param.PopularSearchTermType
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min

@Schema(description = "인기검색어를 검색하기 위한 파라미터")
data class PopularSearchTermAPIParam(
    @field:Min(1, message = "size는 1~10사이의 정수여야 합니다")
    @field:Max(10, message = "size는 1~10사이의 정수여야 합니다")
    @field:Schema(description = "검색할 인기검색어의 수. 1~10사이의 정수. (기본값:10)")
    var size: Int = 10,

    @field:Schema(description = "인기 검색어의 타입(기본값:Raw). Raw(검색어 원문을 기준으로 집계), Keyword(검색어를 분석하여 토큰단위로 집계)", allowableValues = ["Raw", "Keyword"])
    var type: PopularSearchTermType = PopularSearchTermType.Raw,
) {
    fun toBlogSearchParam(): PopularSearchTermParam {
        return PopularSearchTermParam(size, type)
    }

}