package com.kakaobank.blog.api.model.dto

import com.kakaobank.blog.data.store.dto.SearchTermCount
import com.kakaobank.blog.data.store.param.PopularSearchTermType
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "인기검색어 대한 결과")
data class PopularQueryResponseDTO(
    @field:Schema(description = "Raw(검색어 원문 기준), Keyword(검색어를 분리하여 토큰단위로 저장한 것)")
    var type: PopularSearchTermType? = null,
    @field:Schema(description = "검색어(text)와 해당 검색어의 검색횟수(count)를 나타냄")
    var items: List<SearchTermCount> = emptyList(),
)