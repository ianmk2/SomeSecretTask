package com.kakaobank.blog.client.util

import java.io.InputStream
import java.nio.charset.Charset

/**
 * 스트림을 전부 읽어, 문자열로 변경
 */
fun InputStream.readAsString(charset: Charset = Charset.forName("UTF-8")): String {
    val content = StringBuilder()
    this.bufferedReader(charset).use {
        var line = it.readLine()
        while (line != null) {
            content.append(line)
            line = it.readLine()
        }
    }
    return content.toString()
}

