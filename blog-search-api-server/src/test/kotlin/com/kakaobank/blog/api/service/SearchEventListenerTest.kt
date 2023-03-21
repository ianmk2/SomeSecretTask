package com.kakaobank.blog.api.service

import com.kakaobank.blog.data.store.param.SearchTermSaveParam
import com.kakaobank.blog.data.store.service.SearchDataService
import mu.KotlinLogging
import org.junit.jupiter.api.Test
import org.mockito.kotlin.then
import org.mockito.kotlin.times
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.ApplicationEventPublisher
import org.springframework.scheduling.annotation.EnableAsync
import java.time.LocalDateTime


@SpringBootTest(classes = [SearchEventListener::class])
@EnableAsync
class SearchEventListenerTest {

    @Autowired
    lateinit var eventPublisher: ApplicationEventPublisher


    @MockBean
    lateinit var searchDataService: SearchDataService

    val log = KotlinLogging.logger {  }

    @Test
    fun onSearchEventListen() {

        //when
        val saveParam = SearchTermSaveParam("Test", LocalDateTime.now())
        eventPublisher.publishEvent(saveParam)
        log.info { "EVENT전송 후" }
        //then
        Thread.sleep(500)
        then(searchDataService).should(times(1)).saveSearchTerm(saveParam)
    }
}