package com.kakaobank.blog.data.extractor

data class KeywordExtractionResult(
    /**
     * 추출전 원문
     */
    var rawText: String,

    /**
     * 추출해낸 키워드
     */
    var keywords: Set<String>,
)