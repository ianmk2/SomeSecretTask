package com.kakaobank.blog.data.store.service

import com.kakaobank.blog.data.store.dto.SearchTermCount
import com.kakaobank.blog.data.store.dto.SimplePage
import com.kakaobank.blog.data.store.model.SearchHistory
import com.kakaobank.blog.data.store.param.PopularSearchTermParam
import com.kakaobank.blog.data.store.param.PopularSearchTermType
import com.kakaobank.blog.data.store.repo.KeywordCountRepo
import com.kakaobank.blog.data.store.repo.RawQueryCountRepo
import com.kakaobank.blog.data.store.repo.SearchHistoryRepo
import jakarta.validation.Valid
import jakarta.validation.constraints.Min
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.validation.annotation.Validated

@Service
@Validated
class SearchDataQueryService(
    val searchHistoryRepo: SearchHistoryRepo,
    val keywordCountRepo: KeywordCountRepo,
    val rawQueryCountRepo: RawQueryCountRepo,
) {

    /**
     * 인기 키워드를 타입별로 조회
     */
    @Transactional(readOnly = true)
    fun getPopularSearchTerm(param: PopularSearchTermParam): List<SearchTermCount> {
        val items = when (param.type) {
            PopularSearchTermType.Raw -> rawQueryCountRepo.findAllByPopularKeyword(param.size)
            PopularSearchTermType.Keyword -> keywordCountRepo.findAllByPopularKeyword(param.size)
        }
        return items.map {
            SearchTermCount(it.text, it.count)
        }
    }


    @Transactional(readOnly = true)
    fun getSearchHistories(@Valid @Min(1) page: Int, size: Int): SimplePage<SearchHistory> {
        return searchHistoryRepo
            .findAll(
                PageRequest.of(
                    /* page = */ page - 1,
                    /* size = */ size,
                    /* sort = */ Sort.by("createdAt").descending()
                )
            )
            .let {
                SimplePage(it.content, it.totalElements, it.isLast)
            }
    }
}