package com.kakaobank.blog.data.extractor


interface KeywordExtractor {
    fun canExtract(text: String): Boolean
    fun extract(text: String): KeywordExtractionResult
}

class KeywordExtractorChain(
    private val extractors: List<KeywordExtractor>,
) : KeywordExtractor {
    override fun canExtract(text: String): Boolean {
        return extractors.any { it.canExtract(text) }
    }

    override fun extract(text: String): KeywordExtractionResult {
        val aggregateKeywordSet = HashSet<String>()
        extractors.forEach {
            if (it.canExtract(text)) {
                val result = it.extract(text)
                aggregateKeywordSet.addAll(result.keywords)
            }
        }
        return KeywordExtractionResult(
            rawText = text,
            keywords = aggregateKeywordSet,
        )
    }

}