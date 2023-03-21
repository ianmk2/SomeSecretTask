package com.kakaobank.blog.api.service

import com.kakaobank.blog.api.config.CacheNames
import com.kakaobank.blog.api.model.dto.BlogSearchAPIResponseDTO
import com.kakaobank.blog.api.model.dto.PopularQueryResponseDTO
import com.kakaobank.blog.api.model.dto.RecentSearchResponseDTO
import com.kakaobank.blog.api.model.param.BlogSearchAPIParam
import com.kakaobank.blog.api.model.param.PopularSearchTermAPIParam
import com.kakaobank.blog.api.model.param.RecentSearchAPIParam
import com.kakaobank.blog.client.BlogSearchClientChain
import com.kakaobank.blog.data.store.param.SearchTermSaveParam
import com.kakaobank.blog.data.store.service.SearchDataQueryService
import jakarta.validation.Valid
import org.springframework.cache.annotation.Cacheable
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.validation.annotation.Validated
import java.time.LocalDateTime

@Service
@Validated
class BlogService(
    private val blogSearchClient: BlogSearchClientChain,
    private val searchDataQueryService: SearchDataQueryService,
    private val eventPublisher: ApplicationEventPublisher,
) {

    fun searchBlog(@Valid param: BlogSearchAPIParam): BlogSearchAPIResponseDTO {
        val saveParam = SearchTermSaveParam(param.query, LocalDateTime.now())
        //기록이 보틀넥이 되지 않도록 이벤트를 통한 비동기 처리
        eventPublisher.publishEvent(saveParam)
        return blogSearchClient.request(param.toBlogSearchParam(), true, param.source)
            .let { (name, response) -> BlogSearchAPIResponseDTO(name, response) }
    }


    @Cacheable(cacheNames = [CacheNames.PopularSearchTerm], key = "#param.type + ':' + #param.size")
    fun getPopular(@Valid param: PopularSearchTermAPIParam): PopularQueryResponseDTO {
        return searchDataQueryService.getPopularSearchTerm(param.toBlogSearchParam())
            .let { PopularQueryResponseDTO(param.type, it) }
    }



    fun getRecentSearch(param: RecentSearchAPIParam): RecentSearchResponseDTO {
        return searchDataQueryService.getSearchHistories(page = param.page, size = param.size)
            .let { RecentSearchResponseDTO(it) }
    }
}