package com.kakaobank.blog.client.external.naver

import com.kakaobank.blog.client.external.naver.model.NaverBlogSearchParam
import feign.Request
import feign.Response
import java.nio.charset.Charset

class MockNaverAPIClient(
    val statusCode: Int,
    val responseBody: String,
) : NaverAPIClient {

    override fun searchBlog(param: NaverBlogSearchParam): Response {
        return Response.builder()
            .request(Request.create(Request.HttpMethod.GET, "TEST", emptyMap(), ByteArray(0), Charset.forName("UTF-8")))
            .status(statusCode).body(responseBody.toByteArray()).build()
    }
}