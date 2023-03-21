package com.kakaobank.blog.data.extractor

import com.kakaobank.blog.data.extractor.impl.EnglishKeywordExtractor
import com.kakaobank.blog.data.extractor.impl.KoreanKeywordExtractor
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class KeywordExtractorChainTest {


    private val extractor = KeywordExtractorChain(
        listOf(
            KoreanKeywordExtractor(),
            EnglishKeywordExtractor(),
        )
    )

    @ParameterizedTest
    @ValueSource(strings = ["", " ", "*", "ブログ"])
    fun `알파벳과 한글이 섞이지 않은 것은 false를 리턴`(text: String) {
        //given

        //when
        val result = extractor.canExtract(text)

        //then
        assertFalse(result)
    }

    @ParameterizedTest
    @ValueSource(strings = ["한글", "あい 사랑", "abc한글", "b  c"])
    fun `한글 또는 알파벳이 섞인 것은 true를 리턴`(text: String) {
        //given

        //when
        val result = extractor.canExtract(text)

        //then
        assertTrue(result)
    }

    @Test
    fun `한글과 영문 두 언어의 키워드가 추출되어야 한다`() {
        //given

        //when
        val result = extractor.extract("Blog is 블로그")


        //then
        assertEquals(2, result.keywords.size)
        assertEquals(true,  result.keywords.contains("blog"))
        assertEquals(true,  result.keywords.contains("블로그"))
    }
}