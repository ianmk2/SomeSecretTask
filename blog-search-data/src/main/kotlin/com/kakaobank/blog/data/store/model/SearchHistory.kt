package com.kakaobank.blog.data.store.model

import com.kakaobank.blog.data.VALID_QUERY_LENGTH
import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import java.time.LocalDateTime

/**
 * 검색어 원문을 일시와 함께 보관
 */
@Entity
class SearchHistory(
    @field:Id
    @field:GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    /**
     * 검색 원문
     */
    @field:NotBlank
    @field:Column(nullable = false)
    @field:Size(min = 1, max = VALID_QUERY_LENGTH)
    var text: String,

    /**
     * 검색을 한 시간
     * (엔티티의 생성시간이 아님)
     */
    @field:Column(nullable = false)
    var createdAt: LocalDateTime,
) {


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SearchHistory

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}

