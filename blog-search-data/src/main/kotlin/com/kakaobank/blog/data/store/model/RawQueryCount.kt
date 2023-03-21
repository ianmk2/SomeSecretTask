package com.kakaobank.blog.data.store.model

import com.kakaobank.blog.data.VALID_QUERY_LENGTH
import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@Entity
@EntityListeners(value = [AuditingEntityListener::class])
class RawQueryCount(
    @field:Id
    @field:GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    /**
     * 검색어 원문
     */
    @field:NotBlank
    @field:Column(nullable = false, unique = true)
    @field:Size(min = 1, max = VALID_QUERY_LENGTH)
    override var text: String,

    /**
     * 해당 검색어의 검색횟수
     */
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

        other as RawQueryCount

        if (text != other.text) return false

        return true
    }

    override fun hashCode(): Int {
        return text.hashCode()
    }


}
