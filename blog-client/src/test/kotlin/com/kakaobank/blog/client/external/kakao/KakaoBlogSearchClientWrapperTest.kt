package com.kakaobank.blog.client.external.kakao

import com.fasterxml.jackson.databind.ObjectMapper
import com.kakaobank.blog.client.error.BlogAPIClientErrorCode
import com.kakaobank.blog.client.error.BlogAPIClientException
import com.kakaobank.blog.client.external.kakao.model.*
import com.kakaobank.blog.client.model.BlogSearchParam
import com.kakaobank.blog.client.model.BlogSearchSort
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class KakaoBlogSearchClientWrapperTest {
    val dummyKakaoBlogSearchResponseText =
        "{\"documents\":[{\"blogname\":\"프로그래밍 공부 이야기\",\"contents\":\"\\u003cb\\u003e코틀린\\u003c/b\\u003e 인텔리제이, 파이참과 같은 IDE 제작사 젯브레인스에서 개발한 언어로, 자바(JAVA)를 기반언어로 두고 있기 때문에 100% 호환이 가능하며 컴파일시 자바 바이트 파일(.class)이 생성된다. 주로 안드로이드 앱, 스프링부트 앱에서 사용되어 앱 전용 언어로 오해받곤 하지만, 실제로는 다양한 용도로 사용할수 있는...\",\"datetime\":\"2023-03-19T21:11:08.000+09:00\",\"thumbnail\":\"https://search1.kakaocdn.net/argon/130x130_85_c/Hgq4nv9nYis\",\"title\":\"\\u003cb\\u003e코틀린\\u003c/b\\u003e: \\u003cb\\u003e코틀린\\u003c/b\\u003e의 기본 문법\",\"url\":\"http://study-programming-tstory.tistory.com/86\"},{\"blogname\":\"쉽게 접근하는 최신 프로그래밍 언어\",\"contents\":\"\\u003cb\\u003e코틀린\\u003c/b\\u003e 언어를 안드로이드 스튜디오에서도 테스트해 볼 수는 있지만, 좋은 방법은 \\u003cb\\u003e코틀린\\u003c/b\\u003e 전용 개발 환경(IDE)에서 익히는 게 좋습니다. 반대로 \\u003cb\\u003e코틀린\\u003c/b\\u003e 기초를 어느 정도 익힌 다음부터는 안드로이드 스튜디오에서 직접 테스트하는 게 좋습니다. \\u003cb\\u003e코틀린\\u003c/b\\u003e을 실행하기 위해서는 Jet Brains에서 만든 IntelliJ IDEA를...\",\"datetime\":\"2023-03-10T17:19:44.000+09:00\",\"thumbnail\":\"https://search2.kakaocdn.net/argon/130x130_85_c/68ztjKdagFE\",\"title\":\"\\u003cb\\u003e코틀린\\u003c/b\\u003e 설치 및 실행\",\"url\":\"http://yshong60.tistory.com/3\"},{\"blogname\":\"The Yellow Lion King 데이터와  함께 살아가기\",\"contents\":\"모바일 앱을 개발해 보고 싶은 마음이 들어서 유투브를 찾아보던중에 찾은 강의 입니다. 모바일 앱 개발을 위해서는 \\u003cb\\u003e코틀린\\u003c/b\\u003e이라는 새로운 언어를 배워야 하기 때문에 가장 기본적인 입문 내용을 찾아 보았습니다. 자바로도 되지만 요즘 대세는 \\u003cb\\u003e코틀린\\u003c/b\\u003e이라네요. 여러개의 유투브 동영상 강의중 마음에 드는 강의가 있어...\",\"datetime\":\"2022-12-22T12:39:29.000+09:00\",\"thumbnail\":\"https://search3.kakaocdn.net/argon/130x130_85_c/AfwF5QX3AU5\",\"title\":\"\\u003cb\\u003e코틀린\\u003c/b\\u003e 입문\",\"url\":\"http://bigdatamaster.tistory.com/197\"},{\"blogname\":\"은하수 모으기\",\"contents\":\"\\u003cb\\u003e코틀린\\u003c/b\\u003e의 인터페이스를 알아보도록 하자 \\u003cb\\u003e코틀린\\u003c/b\\u003e의 인터페이스는 자바의 인터페이스와는 차이가 있다. \\u003cb\\u003e코틀린\\u003c/b\\u003e의 인터페이스에서는 프로퍼티 선언이 가능하지만 자바는 불가능하다. 자바에는 implements를 통해 인터페이스를 구현하지만 \\u003cb\\u003e코틀린\\u003c/b\\u003e은 콜론(:)을 사용한다. \\u003cb\\u003e코틀린\\u003c/b\\u003e의 인터페이스 안에는 추상 메서드뿐 아니라...\",\"datetime\":\"2023-02-21T22:08:34.000+09:00\",\"thumbnail\":\"https://search2.kakaocdn.net/argon/130x130_85_c/8nNP6S6JR2b\",\"title\":\"[\\u003cb\\u003eKotlin\\u003c/b\\u003e] \\u003cb\\u003e코틀린\\u003c/b\\u003e 인터페이스(Interface)\",\"url\":\"http://interlude.tistory.com/11\"},{\"blogname\":\"SW 공부노트\",\"contents\":\"다른 객체 지향 언어와 비슷하지만 코드의 양을 줄이기 위해 \\u003cb\\u003e코틀린\\u003c/b\\u003e에서만 사용하는 주요 차이점들이 있다. 본격적인 내용에 앞서 용어를 정리해보려 한다. - 클래스(Class): 개체의 청사진 - 객체(Object): 클래스의 인스턴스 - 속성(Property): 클래스의 특성(예를 들면, 수족관의 길이, 너비 및 높이) - 멤버함수...\",\"datetime\":\"2023-03-08T16:00:14.000+09:00\",\"thumbnail\":\"https://search1.kakaocdn.net/argon/130x130_85_c/GpNMFWncZOX\",\"title\":\"[\\u003cb\\u003e코틀린\\u003c/b\\u003e-\\u003cb\\u003eKotlin\\u003c/b\\u003e Bootcamp] 4. Object-oriented programming\",\"url\":\"http://yobinmoksw.tistory.com/21\"},{\"blogname\":\"개발초보 JW의 성장일기\",\"contents\":\"안드로이드 스튜디오 + \\u003cb\\u003e코틀린\\u003c/b\\u003e을 통해 안드로이드 어플 제작을 공부해보려고 한다. 궁금하기도 했고, 알아두면 안드로이드 OS가 돌아가는 방식에 대해 더 알 수 있다는 조언을 들었다. 현 회사에서 유니티로 모바일게임 제작을 하기 때문에, 알아두면 더 좋을 것 같았다. 시작해보자. \\u003cb\\u003e코틀린\\u003c/b\\u003e과 안드로이드 스튜디오...\",\"datetime\":\"2023-02-14T23:46:04.000+09:00\",\"thumbnail\":\"https://search3.kakaocdn.net/argon/130x130_85_c/ByZ24Vl9W7A\",\"title\":\"\\u003cb\\u003e코틀린\\u003c/b\\u003e을 공부해보자 - (1)\",\"url\":\"http://dev-junwoo.tistory.com/121\"},{\"blogname\":\"멘탈 저장소\",\"contents\":\"\\u0026#34;\\u0026lt;한빛미디어 나는 리뷰어다\\u0026gt;\\u0026#34; 활동을 위해서 전자책을 제공받아 작성된 서평입니다. 제목 : 자바에서 \\u003cb\\u003e코틀린\\u003c/b\\u003e으로 - 대상독자 \\u0026#39;이 책은 일차적으로 \\u003cb\\u003e코틀린\\u003c/b\\u003e으로 전환하려는 자바 개발자를 위한 책\\u0026#39; - 옮긴이의 말에 적혀있음 - 책의 내용 및 구성 1. 위 \\u0026#39;대상독자\\u0026#39;에서 언급한 \\u0026#39;옮긴이의 말\\u0026#39; 파트에서 옮긴이는 대상독자...\",\"datetime\":\"2023-02-26T02:15:05.000+09:00\",\"thumbnail\":\"https://search4.kakaocdn.net/argon/130x130_85_c/JlwkhhjHBzJ\",\"title\":\"책 리뷰 : 자바에서 \\u003cb\\u003e코틀린\\u003c/b\\u003e으로\",\"url\":\"http://commontoday.tistory.com/295\"},{\"blogname\":\"개발초보 JW의 성장일기\",\"contents\":\"안의 조건이 false가 될때까지 반복한다. Nullable \\u0026amp; NunNull \\u0026#39;?\\u0026#39; 연산자 NPE (Null Pointer Exception)은 컴파일 시점이 아닌 런타임때 확인이 가능한데, \\u003cb\\u003e코틀린\\u003c/b\\u003e은 \\u0026#39; ? \\u0026#39; 연산자를 이용하여 컴파일시점에 에러를 잡아준다. var name : String = \\u0026#34;철수\\u0026#34; var nullName : String? = null \\u003cb\\u003e코틀린\\u003c/b\\u003e에서 일반적인 String 은...\",\"datetime\":\"2023-02-15T01:13:02.000+09:00\",\"thumbnail\":\"https://search1.kakaocdn.net/argon/130x130_85_c/5Q7MwuTXdNB\",\"title\":\"\\u003cb\\u003e코틀린\\u003c/b\\u003e을 공부해보자 - (2)\",\"url\":\"http://dev-junwoo.tistory.com/122\"},{\"blogname\":\"쉽게 접근하는 최신 프로그래밍 언어\",\"contents\":\"Dalvik)에서 가져오거나, 개발자가 재미있게 봤던 드라마(python) 제목에서 가져온 것처럼, 개발자의 의중이 많이 반영되고 있습니다. 구글이 개발 언어를 자바에서 \\u003cb\\u003e코틀린\\u003c/b\\u003e으로 바꾸기로 한 결정적 이유는 오라클(Oracle)사와의 분쟁 때문입니다. 자바 언어의 소유권을 인수한 오라클은 자사의 소스 코드 11,500줄을...\",\"datetime\":\"2023-03-10T17:22:23.000+09:00\",\"thumbnail\":\"\",\"title\":\"\\u003cb\\u003e코틀린\\u003c/b\\u003e 소개\",\"url\":\"http://yshong60.tistory.com/4\"},{\"blogname\":\"기록하는 습관\",\"contents\":\"** 이 글은 \\u003cb\\u003eKotlin\\u003c/b\\u003e In Action을 읽고 정리한 글입니다. ** 1. 널 가능성 fun strLenSafe(s: String?) = s.length() 타입 이름 뒤에 물음표(?)를 붙이면 그 타입의 변수나 프로퍼티에 null 참조를 저장할 수 있다. val x:String? = null val y:String = x 널이 될 수 있는 값을 널이 될 수 없는 타입의 변수에 대입할 수...\",\"datetime\":\"2023-03-13T23:28:07.000+09:00\",\"thumbnail\":\"https://search2.kakaocdn.net/argon/130x130_85_c/5XFKCXP0oro\",\"title\":\"[\\u003cb\\u003eKotlin\\u003c/b\\u003e In Action]6장 - \\u003cb\\u003e코틀린\\u003c/b\\u003e 타입 시스템\",\"url\":\"http://flexiblecode.tistory.com/233\"}],\"meta\":{\"is_end\":false,\"pageable_count\":800,\"total_count\":60118}}"

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
            val client = MockKakaoAPIClient(statusCode, "")
            val wrapper = KakaoBlogSearchClientWrapper(client, objectMapper, null)

            //when
            val exception = assertThrows<BlogAPIClientException> {
                wrapper.request(KakaoBlogSearchParam("TEST"))
            }

            //then
            assertEquals(errorCode, exception.errorCode)

        }
    }


    @Test
    fun `잘못된 서버의 결과에는 파싱 에러가 일어나야 한다`() {
        //given

        val client = MockKakaoAPIClient(200, "dsf")
        val wrapper = KakaoBlogSearchClientWrapper(client, objectMapper, null)

        //when
        val exception = assertThrows<BlogAPIClientException> {
            wrapper.request(KakaoBlogSearchParam("TEST"))
        }
        //then
        assertEquals(BlogAPIClientErrorCode.ParseFail, exception.errorCode)
    }

    @Test
    fun `파라미터가 올바르게 translate되어야 한다`() {
        //given

        val client = MockKakaoAPIClient(200, dummyKakaoBlogSearchResponseText)
        val wrapper = KakaoBlogSearchClientWrapper(client, objectMapper, null)
        val param = BlogSearchParam("TEST", BlogSearchSort.Recency, 10, 20)

        //when
        val result = wrapper.translateParameter(param)

        //then
        assertEquals("TEST", result.query)
        assertEquals(KakaoBlogSearchSort.recency, result.sort)
        assertEquals(10, result.page)
        assertEquals(20, result.size)
    }

    @Test
    fun `응답이 올바르게 translate되어야 한다`() {
        //given
        val client = MockKakaoAPIClient(200, dummyKakaoBlogSearchResponseText)
        val wrapper = KakaoBlogSearchClientWrapper(client, objectMapper, null)
        val doc = KakaoBlogSearchDocument(
            title = "제목",
            contents = "컨텐츠",
            url = "주소",
            blogName = "블로그 제목",
            thumbnail = "썸네일",
            datetime = OffsetDateTime.of(LocalDateTime.of(2023, 3, 21, 19, 13), ZoneOffset.UTC)
        )
        val meta = KakaoBlogSearchMeta(totalCount = 2, pageableCount = 1, isEnd = true)

        //when
        val result = wrapper.translateResponse(
            KakaoBlogSearchResponse(
                listOf(doc),
                meta,
            )
        )

        //then
        assertEquals(1, result.items.size)
        assertEquals(doc.title, result.items[0].title)
        assertEquals(doc.contents, result.items[0].contents)
        assertEquals(doc.url, result.items[0].url)
        assertEquals(doc.blogName, result.items[0].blogName)
        assertEquals(doc.thumbnail, result.items[0].thumbnail)
        assertEquals(doc.datetime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME), result.items[0].postedAt)
    }

    @Test
    fun `더미 요청에 대해 parsing이 되어야 한다`() {
        //given

        val client = MockKakaoAPIClient(200, dummyKakaoBlogSearchResponseText)
        val wrapper = KakaoBlogSearchClientWrapper(client, objectMapper, null)
        val param = BlogSearchParam("TEST", BlogSearchSort.Recency, 10, 10)

        //when
        val result = wrapper.search(param)

        //then
        assertEquals(10, result.items.size)
        assertEquals(800, result.pagination.totalCount)
        assertEquals(false, result.pagination.isEnd)
    }
}

