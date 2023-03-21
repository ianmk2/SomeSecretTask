package com.kakaobank.blog.client.error

/**
 * 블로그 검색시 발생하는 API에 대한 에러코드
 */
enum class BlogAPIClientErrorCode(
    val message: String,
) {
    InvalidParameter("잘못된 파라미터 요청입니다"),

    ExceedQuota("하루 검색량을 초과하였습니다"),


    ParseFail("데이터 역직렬화에 실패하였습니다"),
    InvalidAPIKey("잘못된 Client API Key입니다"),

    ServicesDown("해당 시스템의 블로그 검색 서비스가 장애 상태입니다"),
    AllBlogServicesDown("이용가능한 블로그 검색 서비스가 없습니다"),
    CircuitBreakOpened("서킷브레이커가 열려있습니다"),

    UnexpectedError("알 수 없는 서버측 에러입니다"),

    ;
}