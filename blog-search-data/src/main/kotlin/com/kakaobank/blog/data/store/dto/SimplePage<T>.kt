package com.kakaobank.blog.data.store.dto

/**
 * SpringData를 사용하지 않는 모듈에서도 사용가능하도록 하는 변환DTO
 */
data class SimplePage<T>(
    val items: List<T>,
    val totalCount: Long,
    val isEnd: Boolean,
)