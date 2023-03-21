package com.kakaobank.blog.api.model.dto

import com.kakaobank.blog.client.model.BlogSearchPagination
import io.swagger.v3.oas.annotations.media.Schema

data class APIPaginationDTO(
    @field:Schema(description = "검색된 항목의 총 수")
    var totalCount: Int,
    @field:Schema(description = "다음 페이지로 검색이 가능한지에 대한 플래그")
    var isEnd: Boolean,
) {
    constructor(pagination: BlogSearchPagination) : this(
        totalCount = pagination.totalCount,
        isEnd = pagination.isEnd,
    )
}