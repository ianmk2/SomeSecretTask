package com.kakaobank.blog.data.store.model

interface CountableText {
    var text: String

    var count: Long

    fun increaseCount()
}