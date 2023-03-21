package com.kakaobank.blog.data.extractor.impl

import com.kakaobank.blog.data.extractor.KeywordExtractionResult
import com.kakaobank.blog.data.extractor.KeywordExtractor
import opennlp.tools.postag.POSModel
import opennlp.tools.postag.POSTaggerME
import opennlp.tools.tokenize.TokenizerME
import opennlp.tools.tokenize.TokenizerModel


class EnglishKeywordExtractor : KeywordExtractor {
    private val alphabetRegex = ".*[a-zA-Z]+.*".toRegex()
    private val tokenizer: TokenizerME
    private val tagger: POSTaggerME

    init {
        //모델 초기화
        javaClass.classLoader.getResourceAsStream("nlp-model/opennlp-en-ud-ewt-pos-1.0-1.9.3.bin").use {
            val modelPOS = POSModel(it)
            tagger = POSTaggerME(modelPOS)
        }
        javaClass.classLoader.getResourceAsStream("nlp-model/opennlp-en-ud-ewt-tokens-1.0-1.9.3.bin").use {
            val modelToken = TokenizerModel(it)
            tokenizer = TokenizerME(modelToken)
        }
    }

    override fun canExtract(text: String): Boolean {
        return isIncludeAlphabet(text)
    }

    override fun extract(text: String): KeywordExtractionResult {
        val normalized = text.lowercase()
        val tokens = tokenizer.tokenize(normalized)
        val tagged = tagger.tag(tokens)
        val result = tokens
            .mapIndexed { index, token ->
                val pos = tagged[index]
                if (pos == "NOUN" && isIncludeAlphabet(token)) {
                    token
                } else {
                    null
                }
            }
            .filterNotNull()
            .toSet()

        return KeywordExtractionResult(
            rawText = text,
            result,
        )
    }


    private fun isIncludeAlphabet(text: String): Boolean {
        return text.matches(alphabetRegex)
    }
}