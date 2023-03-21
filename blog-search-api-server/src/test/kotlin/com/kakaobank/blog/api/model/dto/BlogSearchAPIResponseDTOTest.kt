package com.kakaobank.blog.api.model.dto

import com.kakaobank.blog.client.model.BlogSearchItem
import com.kakaobank.blog.client.model.BlogSearchPagination
import com.kakaobank.blog.client.model.BlogSearchResponse
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class BlogSearchAPIResponseDTOTest {

    @Test
    fun convertDTO() {
        //given
        val item = BlogSearchItem("캐싱된 글", "내용", "https://kakaobank.com", "카카오뱅크", null, "2023-03-21T00:21:22")
        val response = BlogSearchResponse(
            listOf(item),
            BlogSearchPagination(1, true)
        )

        //when
        val result = BlogSearchAPIResponseDTO("Kakao", response)


        //then
        assertEquals("Kakao", result.source)
        assertEquals(1, result.pagination.totalCount)
        assertEquals(true, result.pagination.isEnd)
        assertEquals(item.title, result.items[0].title)
        assertEquals(item.contents, result.items[0].contents)
        assertEquals(item.url, result.items[0].url)
        assertEquals(item.blogName, result.items[0].blogName)
        assertEquals(item.thumbnail, result.items[0].thumbnail)
        assertEquals(item.postedAt, result.items[0].postedAt)

    }
}