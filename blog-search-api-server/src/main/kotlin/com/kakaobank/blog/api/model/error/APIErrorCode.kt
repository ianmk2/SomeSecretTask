@file:JvmName("APIErrorCodeKt")

package com.kakaobank.blog.api.model.error

import com.kakaobank.blog.client.error.BlogAPIClientErrorCode
import org.springframework.http.HttpStatus


enum class APIErrorCode(
    val status: HttpStatus,
    val desc: String? = "",
) {
    InvalidParam(HttpStatus.BAD_REQUEST, "잘못된 파라미터입니다"),
    InternalServerError(HttpStatus.INTERNAL_SERVER_ERROR, "알 수 없는 서버측 에러입니다"),
    ServiceTemporarilyUnavailable(HttpStatus.SERVICE_UNAVAILABLE, "지금은 이용할 수 없습니다. 잠시 후 다시 시도해주십시오"),
    ;

    val fullName: String
        get() = "${this::class.simpleName}::${this.name}"

}

fun BlogAPIClientErrorCode.toAPIErrorCode(): APIErrorCode {
    return when (this) {
        BlogAPIClientErrorCode.InvalidParameter -> APIErrorCode.InvalidParam

        BlogAPIClientErrorCode.ParseFail,
        BlogAPIClientErrorCode.InvalidAPIKey,
        BlogAPIClientErrorCode.UnexpectedError -> APIErrorCode.InternalServerError

        BlogAPIClientErrorCode.ExceedQuota,
        BlogAPIClientErrorCode.ServicesDown,
        BlogAPIClientErrorCode.AllBlogServicesDown,
        BlogAPIClientErrorCode.CircuitBreakOpened -> APIErrorCode.ServiceTemporarilyUnavailable
    }
}
