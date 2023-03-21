package com.kakaobank.blog.api.model.dto

import com.kakaobank.blog.data.store.dto.SimplePage
import com.kakaobank.blog.data.store.model.SearchHistory
import io.swagger.v3.oas.annotations.media.Schema
import java.time.ZoneId
import java.time.ZonedDateTime


@Schema(description = "최근 검색어에 대한 결과")
data class RecentSearchResponseDTO(
    var pagination: APIPaginationDTO,

    var items: List<RecentSearchDTO> = emptyList(),
) {
    constructor(page: SimplePage<SearchHistory>) : this(
        pagination = APIPaginationDTO(page.totalCount.toInt(), page.isEnd),
        items = page.items.map { RecentSearchDTO(it) }
    )
}


@Schema(description = "최근 검색어에 대한 결과")
data class RecentSearchDTO(
    @field:Schema(description = "검색어")
    var text: String,

    @field:Schema(description = "검색을 한 시간")
    var createdAt: ZonedDateTime,
) {
    constructor(item: SearchHistory) : this(
        text = item.text,
        createdAt = item.createdAt.atZone(ZoneId.systemDefault()),
    )
}