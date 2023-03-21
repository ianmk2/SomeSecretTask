package com.kakaobank.blog.api.config

import com.kakaobank.blog.api.model.error.APIErrorCode
import com.kakaobank.blog.api.model.error.APIErrorResponseDTO
import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.servers.Server
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.media.Content
import io.swagger.v3.oas.models.media.MediaType
import io.swagger.v3.oas.models.media.Schema
import io.swagger.v3.oas.models.responses.ApiResponse
import org.springdoc.core.customizers.OpenApiCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@OpenAPIDefinition(servers = [Server(url = "/", description = "카카오 뱅크 - 블로그검색 API")])
@Configuration
class SwaggerConfig {

    @Bean
    fun consumerTypeHeaderOpenAPICustomizer(): OpenApiCustomizer? {
        return OpenApiCustomizer { openApi: OpenAPI ->


            //기본 에러코드가 포함되도록
            openApi.paths.forEach { _, pathItem ->
                pathItem.readOperations().forEach { op ->
                    val responses = op.responses
                    responses.addApiResponse("400", convertApiErrorCodeToApiResponse(APIErrorCode.InvalidParam))
                    responses.addApiResponse("500", convertApiErrorCodeToApiResponse(APIErrorCode.InternalServerError))
                    responses.addApiResponse("503", convertApiErrorCodeToApiResponse(APIErrorCode.ServiceTemporarilyUnavailable))
                }
            }
        }
    }

    private fun convertApiErrorCodeToApiResponse(apiErrorCode: APIErrorCode): ApiResponse {

        val schema = Schema<APIErrorResponseDTO>()
            .example(
                APIErrorResponseDTO(
                    errorCode = "${apiErrorCode.status}",
                    message = apiErrorCode.desc,
                    hint = "SOME HINT",
                )
            )
        val mediaType = MediaType().schema(schema)

        val content = Content().addMediaType("application/json", mediaType)

        val res = ApiResponse()
        res.content = content
        res.description = "[ErrorCode=${apiErrorCode.fullName}] ${apiErrorCode.desc}"
        return res
    }

}