package com.kakaobank.blog.api.service

import com.kakaobank.blog.data.store.param.SearchTermSaveParam
import com.kakaobank.blog.data.store.service.SearchDataService
import mu.KotlinLogging
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component

@Component
class SearchEventListener(
    private val searchDataService: SearchDataService,
) {

    val log = KotlinLogging.logger { }

    /**
     * 비동기적으로 이벤트를 받아 저장
     */
    @EventListener
    @Async
    fun onSearchEventListen(event: SearchTermSaveParam) {
        log.info { "저장 이벤트 받음 : $event" }
        searchDataService.saveSearchTerm(event)

    }

}
