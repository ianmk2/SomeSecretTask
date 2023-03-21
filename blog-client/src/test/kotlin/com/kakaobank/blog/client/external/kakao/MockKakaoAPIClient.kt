package com.kakaobank.blog.client.external.kakao

import com.kakaobank.blog.client.external.kakao.model.KakaoBlogSearchParam
import feign.Request
import feign.Response
import java.nio.charset.Charset

class MockKakaoAPIClient(
    val statusCode: Int,
    val responseBody: String,
) : KakaoAPIClient {
    override fun searchBlog(param: KakaoBlogSearchParam): Response {
        return Response.builder()
            .request(Request.create(Request.HttpMethod.GET, "TEST", emptyMap(), ByteArray(0), Charset.forName("UTF-8")))
            .status(statusCode).body(responseBody.toByteArray()).build()
    }
}