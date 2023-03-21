package com.kakaobank.blog.client.error


class BlogAPIClientException(
    val errorCode: BlogAPIClientErrorCode,
    exception: Exception? = null,

    /**
     * 디버깅에 도움이 되는 해당 서비스의 API Response
     */
    val responseStatusCode: Int? = null,
    val responseBody: Any? = null,
) : RuntimeException(errorCode.message, exception)


