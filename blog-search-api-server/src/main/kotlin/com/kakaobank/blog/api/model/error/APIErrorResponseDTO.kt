package com.kakaobank.blog.api.model.error

import com.fasterxml.jackson.annotation.JsonInclude


data class APIErrorResponseDTO(
    val errorCode: String? = null,
    val message: String? = "ERROR",

    /**
     * 개발자를 위한 힌트(디버기용에 가까움)
     */
    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val hint: Any? = null,
)