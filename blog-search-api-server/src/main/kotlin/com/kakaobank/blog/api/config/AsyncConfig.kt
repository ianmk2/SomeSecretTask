package com.kakaobank.blog.api.config

import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.AsyncConfigurer
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import java.util.concurrent.Executor
import java.util.concurrent.ThreadPoolExecutor

/**
 * 비동기 작업을 위한 스레드풀
 */
@Configuration
class AsyncConfig : AsyncConfigurer {

    override fun getAsyncExecutor(): Executor? {
        val executor = ThreadPoolTaskExecutor()
        executor.setThreadNamePrefix("async-thread-")
        executor.corePoolSize = 10
        executor.maxPoolSize = 50
        executor.setRejectedExecutionHandler(ThreadPoolExecutor.DiscardPolicy())

        executor.initialize()
        return executor

    }

}
