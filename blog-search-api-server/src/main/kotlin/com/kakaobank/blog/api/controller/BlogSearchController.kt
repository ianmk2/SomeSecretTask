package com.kakaobank.blog.api.controller

import com.kakaobank.blog.api.API_PREFIX_V1
import com.kakaobank.blog.api.config.CacheDuration
import com.kakaobank.blog.api.model.dto.BlogSearchAPIResponseDTO
import com.kakaobank.blog.api.model.dto.PopularQueryResponseDTO
import com.kakaobank.blog.api.model.dto.RecentSearchResponseDTO
import com.kakaobank.blog.api.model.param.BlogSearchAPIParam
import com.kakaobank.blog.api.model.param.PopularSearchTermAPIParam
import com.kakaobank.blog.api.model.param.RecentSearchAPIParam
import com.kakaobank.blog.api.service.BlogService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springdoc.core.annotations.ParameterObject
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "Blog", description = "블로그 검색")
@RestController
@RequestMapping(API_PREFIX_V1)
class BlogSearchController(
    private val blogService: BlogService,
) {

    @Operation(
        summary = "인기 검색어",
        description = "가장 인기있는 검색어를 집계하여 제공합니다(집계 결과는 약 ${CacheDuration.PopularSearchTerm}초 동안 캐싱되어 제공됩니다)",
        responses = [
            ApiResponse(responseCode = "200", description = "성공", useReturnTypeSchema = true),
        ]
    )
    @GetMapping("/queries/popular")
    fun getPopularQueries(@ParameterObject @ModelAttribute param: PopularSearchTermAPIParam): PopularQueryResponseDTO {
        return blogService.getPopular(param)
    }

    @Operation(
        summary = "최근 검색어",
        description = "최근에 검색된 검색어를 제공합니다",
        responses = [
            ApiResponse(responseCode = "200", description = "성공", useReturnTypeSchema = true),
        ]
    )
    @GetMapping("/queries")
    fun getRecentSearch(@ParameterObject @ModelAttribute param: RecentSearchAPIParam): RecentSearchResponseDTO {
        return blogService.getRecentSearch(param)
    }


    @Operation(
        summary = "블로그 검색",
        description = "포털사이트의 API를 통해 블로그를 검색합니다.",
        responses = [
            ApiResponse(responseCode = "200", description = "성공", useReturnTypeSchema = true),
        ]
    )
    @GetMapping("/blog")
    fun searchBlog(@ParameterObject @ModelAttribute param: BlogSearchAPIParam): BlogSearchAPIResponseDTO {
        return blogService.searchBlog(param)
    }

}