package com.kakaobank.blog.data.extractor.impl

import com.kakaobank.blog.data.extractor.KeywordExtractionResult
import com.kakaobank.blog.data.extractor.KeywordExtractor
import org.openkoreantext.processor.KoreanPosJava
import org.openkoreantext.processor.OpenKoreanTextProcessorJava

class KoreanKeywordExtractor : KeywordExtractor {

    val hangulCharRegex = ".*[ㄱ-ㅎㅏ-ㅣ가-힣]+.*".toRegex()
    override fun canExtract(text: String): Boolean {
        return isIncludeHangulChar(text)
    }

    override fun extract(text: String): KeywordExtractionResult {

        //분석 전 input data 정규화
        val normalize = OpenKoreanTextProcessorJava.normalize(text)

        //한글이 포함되어 있지 않는 토큰은 이후 분석에서 제외
        val tokens = OpenKoreanTextProcessorJava.tokenize(normalize)
            .filter { isIncludeHangulChar(it.text()) }
        val tokenList = OpenKoreanTextProcessorJava.tokensToJavaKoreanTokenList(tokens)

        //1차적으로 명사만 추출
        val keywords = tokenList
            .filter { it.pos == KoreanPosJava.Noun }
            .map { it.text }
            .toMutableSet()

        //구의 형태로 추출
        val phraseList = OpenKoreanTextProcessorJava.extractPhrases(
            /* tokens = */ tokens,
            /* filterSpam = */ true,
            /* includeHashtags = */ true,
        )
        val nounPhrase = phraseList.filter { it.pos().toString() == "Noun" }.map { it.text() }.toSet()

        keywords.addAll(nounPhrase)

        return KeywordExtractionResult(
            rawText = text,
            keywords = keywords,
        )

    }

    fun isIncludeHangulChar(text: String): Boolean {
        return text.matches(hangulCharRegex)
    }

}