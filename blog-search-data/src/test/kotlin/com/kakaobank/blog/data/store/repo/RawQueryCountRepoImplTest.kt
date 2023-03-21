package com.kakaobank.blog.data.store.repo

import com.kakaobank.blog.data.store.model.RawQueryCount
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RawQueryCountRepoImplTest {

    @Autowired
    lateinit var repo: RawQueryCountRepo

    @Test
    fun `특정 이름으로 저장되어 있는 항목이 있다면 그것으로 검색이 되어야 한다`() {
        //given
        repo.save(RawQueryCount(text = "테스트"))

        //when
        val result = repo.findByTextWithLock(text = "테스트")

        //then

        Assertions.assertEquals("테스트", result?.text)
    }

    @Test
    fun `인기순으로 size만큼 리턴되어야 한다`() {
        //given
        repo.save(RawQueryCount(text = "테스트3", count = 3))
        repo.save(RawQueryCount(text = "테스트1", count = 1))
        repo.save(RawQueryCount(text = "테스트2", count = 2))
        repo.save(RawQueryCount(text = "테스트4", count = 0))

        //when
        val result = repo.findAllByPopularKeyword(3)

        //then
        Assertions.assertEquals("테스트3", result[0].text)
        Assertions.assertEquals(3L, result[0].count)
        Assertions.assertEquals("테스트2", result[1].text)
        Assertions.assertEquals(2L, result[1].count)
        Assertions.assertEquals("테스트1", result[2].text)
        Assertions.assertEquals(1L, result[2].count)
    }

}