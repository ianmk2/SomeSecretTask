package com.kakaobank.blog.api.controller

import com.kakaobank.blog.api.model.dto.*
import com.kakaobank.blog.api.service.BlogService
import com.kakaobank.blog.data.store.dto.SearchTermCount
import com.kakaobank.blog.data.store.param.PopularSearchTermType
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.mockito.kotlin.any
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import java.time.OffsetDateTime
import java.time.ZonedDateTime

@WebMvcTest(BlogSearchController::class)
class BlogSearchControllerTest {
    @Autowired
    lateinit var mockMvc: MockMvc

    @MockBean
    lateinit var service: BlogService

    @Test
    fun getPopularQueries() {

        //given
        given(service.getPopular(any())).willReturn(
            PopularQueryResponseDTO(
                PopularSearchTermType.Keyword,
                items = listOf(SearchTermCount("A", 2))
            )
        )


        //when & then
        mockMvc
            .get("/api/v1/queries/popular?size=1&type=Keyword")
            .andDo { print() }
            .andExpect {
                status { isOk() }
                content {
                    contentType(MediaType.APPLICATION_JSON)
                    jsonPath("$.type") { value("Keyword") }
                    jsonPath("$.items.length()") { value(1) }
                    jsonPath("$.items[0].text") { value("A") }
                    jsonPath("$.items[0].count") { value(2) }
                }
            }
    }

    @Test
    fun getRecentSearch() {

        //given
        given(service.getRecentSearch(any())).willReturn(
            RecentSearchResponseDTO(
                items = listOf(
                    RecentSearchDTO("B", ZonedDateTime.now()),
                    RecentSearchDTO("A", ZonedDateTime.now().minusHours(1))
                ),
                pagination = APIPaginationDTO(2, true),
            )
        )


        //when & then
        mockMvc
            .get("/api/v1/queries?size=2")
            .andDo { print() }
            .andExpect {
                status { isOk() }
                content {
                    contentType(MediaType.APPLICATION_JSON)
                    jsonPath("$.pagination.totalCount") { value(2) }
                    jsonPath("$.pagination.isEnd") { value(true) }
                    jsonPath("$.items.length()") { value(2) }
                    jsonPath("$.items[0].text") { value("B") }
                    jsonPath("$.items[1].text") { value("A") }
                }
            }
    }

    @Test
    fun searchBlog() {

        //given
        given(service.searchBlog(any())).willReturn(
            BlogSearchAPIResponseDTO(
                items = listOf(
                    BlogDocumentDTO(
                        title = "Title",
                        contents = "Contents",
                        url = "URL",
                        blogName = "BlogName",
                        thumbnail = "Thumbnail",
                        postedAt = "2023-03-21T10:00:00",
                    )
                ),
                pagination = APIPaginationDTO(
                    totalCount = 2,
                    isEnd = true,
                ),
                source = "Kakao"
            )
        )


        //when & then
        mockMvc
            .get("/api/v1/blog?query=TEST&sort=Recency&page=2&size=1")
            .andDo { print() }
            .andExpect {
                status { isOk() }
                content {
                    contentType(MediaType.APPLICATION_JSON)
                    jsonPath("$.source") { value("Kakao") }

                    jsonPath("$.items.length()") { value(1) }
                    jsonPath("$.items[0].title") { value("Title") }
                    jsonPath("$.items[0].contents") { value("Contents") }
                    jsonPath("$.items[0].url") { value("URL") }
                    jsonPath("$.items[0].blogName") { value("BlogName") }
                    jsonPath("$.items[0].thumbnail") { value("Thumbnail") }
                    jsonPath("$.items[0].postedAt") { value("2023-03-21T10:00:00") }

                    jsonPath("$.pagination.totalCount") { value(2) }
                    jsonPath("$.pagination.isEnd") { value(true) }

                }
            }
    }
}
