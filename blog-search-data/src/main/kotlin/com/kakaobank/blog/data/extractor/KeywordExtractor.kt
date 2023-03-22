package com.kakaobank.blog.data.extractor


interface KeywordExtractor {
    /**
     * 해당 문장에서 추출해낼 수 있는지에 대한 체크
     */
    fun canExtract(text: String): Boolean

    /**
     * 해당 문장에서 키워드를 추출함
     */
    fun extract(text: String): KeywordExtractionResult
}

/**
 * 여러 KeywordExtractor를 체이닝하여 호출하며 키워드를 추출한다
 */
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