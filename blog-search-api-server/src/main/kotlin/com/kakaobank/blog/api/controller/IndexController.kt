package com.kakaobank.blog.api.controller

import io.swagger.v3.oas.annotations.Operation
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class IndexController {

    @Operation(hidden = true)
    @GetMapping("/")
    fun index(): Map<String, String> {
        return mapOf("message" to "BlogSearch")
    }
}