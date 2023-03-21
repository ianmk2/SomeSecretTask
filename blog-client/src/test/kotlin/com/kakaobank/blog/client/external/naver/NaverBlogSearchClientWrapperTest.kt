package com.kakaobank.blog.client.external.naver

import com.fasterxml.jackson.databind.ObjectMapper
import com.kakaobank.blog.client.error.BlogAPIClientErrorCode
import com.kakaobank.blog.client.error.BlogAPIClientException
import com.kakaobank.blog.client.external.naver.model.NaverBlogSearchItem
import com.kakaobank.blog.client.external.naver.model.NaverBlogSearchParam
import com.kakaobank.blog.client.external.naver.model.NaverBlogSearchResponse
import com.kakaobank.blog.client.external.naver.model.NaverBlogSearchSort
import com.kakaobank.blog.client.model.BlogSearchParam
import com.kakaobank.blog.client.model.BlogSearchSort
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class NaverBlogSearchClientWrapperTest {
    val dummyNaverBlogSearchResponseText =
        "{\t\"lastBuildDate\":\"Mon, 20 Mar 2023 01:00:58 +0900\",\t\"total\":16075,\t\"start\":1,\t\"display\":10,\t\"items\":[\t\t{\t\t\t\"title\":\"&apos;자바에서 <b>코틀린<\\/b>으로&apos;, <b>코틀린<\\/b>을 <b>코틀린<\\/b> 답게 쓰는... \",\t\t\t\"link\":\"https:\\/\\/blog.naver.com\\/theparanbi\\/223028479594\",\t\t\t\"description\":\"그러기에 이번에 본, 자바를 <b>코틀린<\\/b>으로 바꾸는 각종 노하우를 담고 있는 '자바에서 <b>코틀린<\\/b>으로'는 #리팩터링, 마이그레이션 측면에서 너무나도 가치 있는 책이라 생각한다. 이 책을 통해 내 자바와 <b>코틀린<\\/b>... \",\t\t\t\"bloggername\":\"더파란비 (theparanbi)의 블로그\",\t\t\t\"bloggerlink\":\"blog.naver.com\\/theparanbi\",\t\t\t\"postdate\":\"20230226\"\t\t},\t\t{\t\t\t\"title\":\"신입 개발자의 <b>코틀린<\\/b> 완벽 가이드 (길벗) 리뷰\",\t\t\t\"link\":\"https:\\/\\/blog.naver.com\\/dbqls01477\\/222697075333\",\t\t\t\"description\":\"그럼에서 내가 이번에 접하게 된 길벗의 개발서는 <b>코틀린<\\/b> 완벽 가이드 라는 책이다. 제목에서도 알 수 있듯 <b>코틀린<\\/b> 언어에 대한 책이다. 먼저 나의 경험을 수줍게 언급하자면,, 자바는 학부 수준이고 자바... \",\t\t\t\"bloggername\":\"스몰사이즈룸\",\t\t\t\"bloggerlink\":\"blog.naver.com\\/dbqls01477\",\t\t\t\"postdate\":\"20220410\"\t\t},\t\t{\t\t\t\"title\":\"[Kotlin(<b>코틀린<\\/b>)] if-else 조건문과 when 조건문\",\t\t\t\"link\":\"https:\\/\\/blog.naver.com\\/smhrd_official\\/223023976856\",\t\t\t\"description\":\"오늘은 <b>코틀린<\\/b>에서 조건문을 사용하는 방법에 대해 알아보도록 할 건데요. 그럼 먼저 조건문이... <b>코틀린<\\/b>에서의 조건문은 if-else와 When 두 가지가 있는데요. if 문은 연도같이 범위가 넓고 값을 특정할 수... \",\t\t\t\"bloggername\":\"스마트인재개발원\",\t\t\t\"bloggerlink\":\"blog.naver.com\\/smhrd_official\",\t\t\t\"postdate\":\"20230222\"\t\t},\t\t{\t\t\t\"title\":\"<b>코틀린<\\/b>의 연산자 (자바 강좌 연재)(011 번외) - 하이미디어 성남... \",\t\t\t\"link\":\"https:\\/\\/blog.naver.com\\/eekdland\\/223039997609\",\t\t\t\"description\":\"이번 시간에는 <b>코틀린<\\/b>의 연산자를 다루어보도록 하겠습니다~ 이 글에서는 <b>코틀린<\\/b>의 연산자를 요약하여 다루므로 각 연산자에 대한 자세한 설명은 아래 자바스크립트 강좌들을 참고해주세요.... \",\t\t\t\"bloggername\":\"[CUE] 신세기 SSG 연구소\",\t\t\t\"bloggerlink\":\"blog.naver.com\\/eekdland\",\t\t\t\"postdate\":\"20230310\"\t\t},\t\t{\t\t\t\"title\":\"[서평] 개발자를 위한 <b>코틀린<\\/b> 프로그래밍\",\t\t\t\"link\":\"https:\\/\\/blog.naver.com\\/skylapunjel\\/222891124664\",\t\t\t\"description\":\"[서평] 개발자를 위한 <b>코틀린<\\/b> 프로그래밍 인공지능이 많이 발전한 것 같습니다. 얼마전에 유럽에 어느... <b>코틀린<\\/b>은 앱과 웹이 모두 가능한 언어라 합니다. 인텔리제이라는 개발 툴을 만드는 젯브레인사에서 만든... \",\t\t\t\"bloggername\":\"skylapunjel&apos;s 공간\",\t\t\t\"bloggerlink\":\"blog.naver.com\\/skylapunjel\",\t\t\t\"postdate\":\"20221004\"\t\t},\t\t{\t\t\t\"title\":\"안드로이드 앱 개발을 위한 <b>코틀린<\\/b>(Kotlin)알아보기!\",\t\t\t\"link\":\"https:\\/\\/blog.naver.com\\/ithopenanum\\/222613540137\",\t\t\t\"description\":\"오늘은 안드로이드 앱 개발에 사용되는 언어인 <b>코틀린<\\/b>(Kotlin)에 대해 알아보려고 하는데요. 자바(Java)랑 차이점은 무엇인지, <b>코틀린<\\/b>은 어떤 특징을 가지고 있는지 등을 자세히 알아보려고 합니다. 그럼... \",\t\t\t\"bloggername\":\"나셀프\",\t\t\t\"bloggerlink\":\"blog.naver.com\\/ithopenanum\",\t\t\t\"postdate\":\"20220105\"\t\t},\t\t{\t\t\t\"title\":\"&lt;아토믹 <b>코틀린<\\/b>&gt; <b>코틀린<\\/b> 컴퍼일러 개발자가 알려주는 <b>코틀린<\\/b>... \",\t\t\t\"link\":\"https:\\/\\/blog.naver.com\\/gilbutzigy\\/223037509153\",\t\t\t\"description\":\"<b>코틀린<\\/b> 컴파일러 개발자가 알려주는 <b>코틀린<\\/b> 기본기 87 아토믹 <b>코틀린<\\/b> <b>코틀린<\\/b>을 익히고 레벨업하는 가장 확실한 방법! 한 번에 하나씩 + 명확한 예제를 실행하면서 + 훌륭한 멘토의 설명으로... \",\t\t\t\"bloggername\":\"직장인을 위한 길벗 실용서\",\t\t\t\"bloggerlink\":\"blog.naver.com\\/gilbutzigy\",\t\t\t\"postdate\":\"20230307\"\t\t},\t\t{\t\t\t\"title\":\"오준석의 안드로이드 생존코딩 <b>코틀린<\\/b>편(한빛미디어)\",\t\t\t\"link\":\"https:\\/\\/blog.naver.com\\/owl10owl\\/222654651106\",\t\t\t\"description\":\"바로 '오준석의 안드로이드 생존코딩 <b>코틀린<\\/b>편'입니다. 이 책은 프로그래밍에서 걸음마를 뗀 사람, 혹은... 따라하다보면 <b>코틀린<\\/b>을 쉽게 접하고 익힐 수 있습니다. 또 이 책은 안드로이드 스튜디오와 <b>코틀린<\\/b>... \",\t\t\t\"bloggername\":\"올빼미간이역\",\t\t\t\"bloggerlink\":\"blog.naver.com\\/owl10owl\",\t\t\t\"postdate\":\"20220222\"\t\t},\t\t{\t\t\t\"title\":\"[Kotlin in Action] 1.<b>코틀린<\\/b>이란 무엇이며 왜 필요한가\",\t\t\t\"link\":\"https:\\/\\/blog.naver.com\\/yewon7036\\/223005775299\",\t\t\t\"description\":\"<b>코틀린<\\/b> 주요 특성 정적 타입 지정 언어 자바와 마찬가지로 <b>코틀린<\\/b>도 정적 타입 (statically typed) 지정 언어 모든 프로그램 구성요소의 타입을 컴파일 시점에 알 수 있음 객체의 필드나 메서드를 사용할때... \",\t\t\t\"bloggername\":\"고라니는 코딩중\",\t\t\t\"bloggerlink\":\"blog.naver.com\\/yewon7036\",\t\t\t\"postdate\":\"20230205\"\t\t},\t\t{\t\t\t\"title\":\"깡샘의 안드로이드 앱 프로그래밍 with <b>코틀린<\\/b> \\/ 16~39p... \",\t\t\t\"link\":\"https:\\/\\/blog.naver.com\\/passgiant\\/223045399086\",\t\t\t\"description\":\"깡샘의 안드로이드 앱 프로그래밍 with <b>코틀린<\\/b> 저자 강성윤 출판 이지스퍼블리싱 발매 2022.12.16. 2. 나의 스터디 흔적을 사진으로 보여주세요. 책상(모니터)과 책이 함께 보이면 최고! 3. 이번 스터디에서... \",\t\t\t\"bloggername\":\"passgiant\",\t\t\t\"bloggerlink\":\"blog.naver.com\\/passgiant\",\t\t\t\"postdate\":\"20230315\"\t\t}\t]}"

    val objectMapper = ObjectMapper().findAndRegisterModules()

    @Test
    fun `각 에러 status code에 맞는 exception이 일어나야 한다`() {
        //given
        val errorCodeMap = mapOf(
            400 to BlogAPIClientErrorCode.InvalidParameter,
            429 to BlogAPIClientErrorCode.ExceedQuota,
            500 to BlogAPIClientErrorCode.ServicesDown,
        )

        errorCodeMap.forEach { (statusCode, errorCode) ->
            val client = MockNaverAPIClient(statusCode, "")
            val wrapper = NaverBlogSearchClientWrapper(client, objectMapper, null)

            //when
            val exception = assertThrows<BlogAPIClientException> {
                wrapper.request(NaverBlogSearchParam("TEST"))
            }

            //then
            Assertions.assertEquals(errorCode, exception.errorCode)

        }
    }


    @Test
    fun `잘못된 서버의 결과에는 파싱 에러가 일어나야 한다`() {
        //given

        val client = MockNaverAPIClient(200, "dsf")
        val wrapper = NaverBlogSearchClientWrapper(client, objectMapper, null)

        //when
        val exception = assertThrows<BlogAPIClientException> {
            wrapper.request(NaverBlogSearchParam("TEST"))
        }
        //then
        Assertions.assertEquals(BlogAPIClientErrorCode.ParseFail, exception.errorCode)
    }

    @Test
    fun `파라미터가 올바르게 translate되어야 한다`() {
        //given
        val client = MockNaverAPIClient(200, dummyNaverBlogSearchResponseText)
        val wrapper = NaverBlogSearchClientWrapper(client, objectMapper, null)
        val param = BlogSearchParam("TEST", BlogSearchSort.Recency, 3, 2)

        //when
        val result = wrapper.translateParameter(param)

        //then
        Assertions.assertEquals("TEST", result.query)
        Assertions.assertEquals(NaverBlogSearchSort.date, result.sort)
        Assertions.assertEquals(5, result.start)
        Assertions.assertEquals(2, result.display)
    }

    @Test
    fun `응답이 올바르게 translate되어야 한다`() {
        //given
        val client = MockNaverAPIClient(200, dummyNaverBlogSearchResponseText)
        val wrapper = NaverBlogSearchClientWrapper(client, objectMapper, null)
        val doc = NaverBlogSearchItem(
            title = "제목",
            description = "컨텐츠",
            link = "주소",
            bloggerName = "블로그 제목",
            bloggerLink = "블로그 주소",
            postdate = LocalDate.now()
        )

        //when
        val result = wrapper.translateResponse(
            NaverBlogSearchResponse(
                lastBuildDate = "",
                total = 2,
                start = 1,
                display = 1,
                listOf(doc),
            )
        )

        //then
        Assertions.assertEquals(1, result.items.size)
        Assertions.assertEquals(doc.title, result.items[0].title)
        Assertions.assertEquals(doc.description, result.items[0].contents)
        Assertions.assertEquals(doc.link, result.items[0].url)
        Assertions.assertEquals(doc.bloggerName, result.items[0].blogName)
        Assertions.assertEquals(null, result.items[0].thumbnail)
        Assertions.assertEquals(LocalDate.now().format(DateTimeFormatter.ISO_DATE), result.items[0].postedAt)
    }

    @Test
    fun `더미 요청에 대해 parsing이 되어야 한다`() {
        //given

        val client = MockNaverAPIClient(200, dummyNaverBlogSearchResponseText)
        val wrapper = NaverBlogSearchClientWrapper(client, objectMapper, null)
        val param = BlogSearchParam("TEST", BlogSearchSort.Recency, 10, 10)

        //when
        val result = wrapper.search(param)

        //then
        Assertions.assertEquals(10, result.items.size)
        Assertions.assertEquals(16075, result.pagination.totalCount)
        Assertions.assertEquals(false, result.pagination.isEnd)
    }
}

