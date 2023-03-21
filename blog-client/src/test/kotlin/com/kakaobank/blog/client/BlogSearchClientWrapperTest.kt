package com.kakaobank.blog.client

import com.kakaobank.blog.client.model.BlogSearchItem
import com.kakaobank.blog.client.model.BlogSearchPagination
import com.kakaobank.blog.client.model.BlogSearchParam
import com.kakaobank.blog.client.model.BlogSearchResponse
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.mockito.kotlin.*
import org.springframework.cache.Cache

class BlogSearchClientWrapperTest {

    @Test
    fun `캐시를 사용 한 경우 두번째 같은 요청은 캐시에서 응답되어야 한다`() {
        //given
        val mockCache = mock<Cache>()
        val wrapper = spy(MockClientWrapper(cache = mockCache))
        val response = BlogSearchResponse(
            listOf(BlogSearchItem("캐싱된 글", "내용", "https://kakaobank.com", "카카오뱅크", null, "2023-03-21T00:21:22")),
            BlogSearchPagination(1, true)
        )
        var keyName: String = ""
        given(mockCache.get(any(), eq(BlogSearchResponse::class.java)))
            .willAnswer {
                keyName = it.arguments[0] as String
                null
            }
            .willReturn { response }


        //when
        val result1 = wrapper.search(BlogSearchParam("Cache"), useCache = true)
        val result2 = wrapper.search(BlogSearchParam("Cache"), useCache = true)

        //then
        then(mockCache).should(times(2)).get(any(), eq(BlogSearchResponse::class.java))
        then(mockCache).should(times(1)).put(eq(keyName), any())
        then(wrapper).should(times(1)).request(any())


    }

    @Test
    fun `캐시를 사용 한 경우 두번째의 다른 요청은 캐시에서 응답되어선 안된다`() {
        //given
        val mockCache = mock<Cache>()
        val wrapper = spy(MockClientWrapper(cache = mockCache))

        var keyName1: String = ""
        var keyName2: String = ""
        given(mockCache.get(any(), eq(BlogSearchResponse::class.java)))
            .willAnswer {
                keyName1 = it.arguments[0] as String
                null
            }
            .willAnswer {
                keyName2 = it.arguments[0] as String
                null
            }


        //when
        wrapper.search(BlogSearchParam("Cache"), useCache = true)
        wrapper.search(BlogSearchParam("Cache2"), useCache = true)

        //then
        then(mockCache).should(times(2)).get(any(), eq(BlogSearchResponse::class.java))
        then(mockCache).should(times(2)).put(any(), any())
        then(wrapper).should(times(2)).request(any())
        Assertions.assertNotEquals(keyName1, keyName2)

    }

    @Test
    fun `캐시를 사용하지 않은 경우 항상 새로 요청되어야 한다`() {
        //given

        val wrapper = spy(MockClientWrapper())

        var keyName1: String = ""
        var keyName2: String = ""

        //when
        wrapper.search(BlogSearchParam("Cache"), useCache = true)
        wrapper.search(BlogSearchParam("Cache"), useCache = true)

        //then
        then(wrapper).should(times(2)).request(any())
    }
}