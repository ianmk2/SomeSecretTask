package com.kakaobank.blog.client

import com.kakaobank.blog.client.error.BlogAPIClientErrorCode
import com.kakaobank.blog.client.error.BlogAPIClientException
import com.kakaobank.blog.client.model.BlogSearchParam
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.spy
import org.mockito.kotlin.then

class BlogSearchClientChainTest {

    @Test
    fun `앞선 클라이언트가 실패 하는 경우 요청이 fallback 되어 시도해야 한다`() {

        //given
        //항상 실패하는 클라이언트1, 항상 성공하는 클라이언트2
        val client1 = spy(MockClientWrapper(alwaysFailWithErrorCode = BlogAPIClientErrorCode.ServicesDown, sourceName = "Mock1"))
        val client2 = spy(MockClientWrapper(sourceName = "Mock2"))


        //when
        val chain = BlogSearchClientChain(listOf(client1, client2))
        val param = BlogSearchParam(query = "테스트")
        val (sourceName, response) = chain.request(param)

        //then
        assertEquals("Mock2", sourceName)
        then(client1).should().search(param)
        then(client2).should().search(param)
    }


    @Test
    fun `모든 클라이언트가 실패하면 에러가 발생해야 한다`() {

        //given
        val client1 = spy(MockClientWrapper(alwaysFailWithErrorCode = BlogAPIClientErrorCode.ServicesDown, sourceName = "Mock1"))
        val client2 = spy(MockClientWrapper(alwaysFailWithErrorCode = BlogAPIClientErrorCode.ServicesDown, sourceName = "Mock2"))


        //when
        val chain = BlogSearchClientChain(listOf(client1, client2))
        val param = BlogSearchParam(query = "테스트")
        val exception = assertThrows<BlogAPIClientException> {
            val (sourceName, response) = chain.request(param)
        }

        //then
        assertEquals(BlogAPIClientErrorCode.AllBlogServicesDown, exception.errorCode)
    }


    @Test
    fun `특정 source를 지정한 경우 후순위여도 해당 클라이언트가 선택되어야 한다`() {

        //given
        val client1 = spy(MockClientWrapper(sourceName = "Mock1"))
        val client2 = spy(MockClientWrapper(sourceName = "Mock2"))


        //when
        val chain = BlogSearchClientChain(listOf(client1, client2))
        val param = BlogSearchParam(query = "테스트")
        val (sourceName, response) = chain.request(param, source = "Mock2")

        //then
        assertEquals("Mock2", sourceName)
    }

    @Test
    fun `특정 source를 지정한 경우 해당 클라이언트가 에러를 발생시킨 경우 해당 에러가 에스컬레이션 되어야 한다`() {

        //given
        val client1 = spy(MockClientWrapper(sourceName = "Mock1"))
        val client2 = spy(MockClientWrapper(alwaysFailWithErrorCode = BlogAPIClientErrorCode.InvalidParameter, sourceName = "Mock2"))


        //when
        val chain = BlogSearchClientChain(listOf(client1, client2))
        val param = BlogSearchParam(query = "테스트")
        val exception = assertThrows<BlogAPIClientException> {
            val (sourceName, response) = chain.request(param, source = "Mock2")
        }

        //then
        assertEquals(BlogAPIClientErrorCode.InvalidParameter, exception.errorCode)
    }
}