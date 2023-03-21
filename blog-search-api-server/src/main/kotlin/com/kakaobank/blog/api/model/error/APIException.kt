package com.kakaobank.blog.api.model.error

class APIException : RuntimeException {
    val errorCode: APIErrorCode

    /**
     * 디버깅을 위한 힌트 오브젝트
     */
    val hint: Any?

    constructor(errorCode: APIErrorCode, hint: Any? = null) : super(errorCode.desc) {
        this.errorCode = errorCode
        this.hint = hint
    }

}