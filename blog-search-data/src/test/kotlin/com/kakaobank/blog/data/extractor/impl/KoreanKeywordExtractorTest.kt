package com.kakaobank.blog.data.extractor.impl

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class KoreanKeywordExtractorTest {

    @ParameterizedTest
    @ValueSource(strings= ["", "abd", "*"])
    fun `한글이 섞이지 않은 것은 false를 리턴`(text:String) {
        //given

        //when
        val extractor = KoreanKeywordExtractor()
        val result = extractor.canExtract(text)

        //then
        assertFalse(result)
    }

    @ParameterizedTest
    @ValueSource(strings= ["한글", "abc한글", "한  글"])
    fun `한글이 섞인 것은 true를 리턴`(text:String) {
        //given

        //when
        val extractor = KoreanKeywordExtractor()
        val result = extractor.canExtract(text)

        //then
        assertTrue(result)
    }

    @Test
    fun `한글 키워드가 추출되어야 한다`() {
        //given

        //when
        val extractor = KoreanKeywordExtractor()
        val result = extractor.extract("블로그 검색")

        //then
        assertEquals(3, result.keywords.size)
        assertTrue(result.keywords.contains("블로그"))
        assertTrue(result.keywords.contains("검색"))
        assertTrue(result.keywords.contains("블로그검색"))
    }}