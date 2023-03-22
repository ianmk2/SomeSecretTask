package com.kakaobank.blog.data.store.model

import com.kakaobank.blog.data.VALID_QUERY_LENGTH
import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime


/**
 * 검색어에서 키워드를 추출하여 키워드별로 집계하여 보관
 */
@Entity
@EntityListeners(value = [AuditingEntityListener::class])
class KeywordCount(
    @field:Id
    @field:GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    /**
     * 토큰 단위 또는 구의 단위로 잘려진 키워드
     */
    @field:NotBlank
    @field:Column(nullable = false, unique = true)
    @field:Size(min = 1, max = VALID_QUERY_LENGTH)
    override var text: String,

    /**
     * 해당 키워드의 검색횟수
     */
    @field:Column(nullable = false)
    override var count: Long = 0,

    @field:CreatedDate
    @field:Column(nullable = false)
    var createdAt: LocalDateTime? = null,

    @field:LastModifiedDate
    @field:Column(nullable = false)
    var updatedAt: LocalDateTime? = null,
) : CountableText {

    override fun increaseCount() {
        count++
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as KeywordCount

        if (text != other.text) return false

        return true
    }

    override fun hashCode(): Int {
        return text.hashCode()
    }


}

