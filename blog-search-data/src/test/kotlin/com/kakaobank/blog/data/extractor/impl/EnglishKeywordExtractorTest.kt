package com.kakaobank.blog.data.extractor.impl

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource


class EnglishKeywordExtractorTest {


    @ParameterizedTest
    @ValueSource(strings= ["", "한글", "*"])
    fun `영문이 섞이지 않은 것은 false를 리턴`(text:String) {
        //given

        //when
        val extractor = EnglishKeywordExtractor()
        val result = extractor.canExtract(text)

        //then
        assertFalse(result)
    }

    @ParameterizedTest
    @ValueSource(strings= ["a", "abc한글", "b  c"])
    fun `알파벳이 섞인 것은 true를 리턴`(text:String) {
        //given

        //when
        val extractor = EnglishKeywordExtractor()
        val result = extractor.canExtract(text)

        //then
        assertTrue(result)
    }

    @Test
    fun `영문 키워드가 추출되어야 한다`() {
        //given

        //when
        val extractor = EnglishKeywordExtractor()
        val result = extractor.extract("Fancy Blog    ")

        //then
        assertEquals(1, result.keywords.size)
        assertEquals("blog", result.keywords.first())
    }
}