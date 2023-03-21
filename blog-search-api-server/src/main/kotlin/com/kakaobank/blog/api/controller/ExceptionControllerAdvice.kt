package com.kakaobank.blog.api.controller

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.kakaobank.blog.api.model.error.*
import com.kakaobank.blog.client.error.BlogAPIClientException
import mu.KotlinLogging
import org.apache.commons.lang3.exception.ExceptionUtils
import org.springframework.beans.TypeMismatchException
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.ServletRequestBindingException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.multipart.MultipartException
import org.springframework.web.multipart.support.MissingServletRequestPartException

@ControllerAdvice
class ExceptionControllerAdvice(
    private val objectMapper: ObjectMapper
) {
    val log = KotlinLogging.logger { }


    @ExceptionHandler(APIException::class)
    protected fun handleAPIExceptions(ex: APIException, request: WebRequest?): ResponseEntity<Any> {
        val response = APIErrorResponseDTO(
            errorCode = ex.errorCode.fullName,
            hint = ex.hint,
            message = ex.message
        )
        val status: Int = ex.errorCode.status.value()
        return responseException(status, response)
    }

    @ExceptionHandler(BlogAPIClientException::class)
    protected fun handleBlogAPIExceptions(ex: BlogAPIClientException, request: WebRequest?): ResponseEntity<Any> {
        log.error(ex) { ex.message }
        return handleAPIExceptions(APIException(ex.errorCode.toAPIErrorCode(), hint = ex.responseBody), request)
    }

    @ExceptionHandler(
        MultipartException::class,
        ServletRequestBindingException::class,
        BindException::class,
        MissingServletRequestPartException::class,
        MethodArgumentNotValidException::class,
        MissingServletRequestParameterException::class,
        TypeMismatchException::class
    )
    protected fun generalException(ex: Exception, request: WebRequest?): ResponseEntity<Any> {
        val errorCode: APIErrorCode = APIErrorCode.InvalidParam

        val response = APIErrorResponseDTO(
            errorCode = errorCode.fullName,
            message = errorCode.desc,
            hint = ex.message,
        )

        return responseException(errorCode.status.value(), response)
    }

    @ExceptionHandler(Exception::class)
    protected fun exception(ex: Exception, request: WebRequest?): ResponseEntity<Any> {

        log.error(ex.message, ex)
        val errorCode: APIErrorCode = APIErrorCode.InternalServerError

        val response = APIErrorResponseDTO(
            errorCode = errorCode.fullName,
            message = errorCode.desc,
            hint = ex.message
        )

        return responseException(errorCode.status.value(), response)
    }

    private fun responseException(statusCode: Int, body: Any): ResponseEntity<Any> {
        return try {
            val json: String = objectMapper.writeValueAsString(body)
            ResponseEntity.status(statusCode).contentType(MediaType.APPLICATION_JSON).body(json)
        } catch (e: JsonProcessingException) {
            throw RuntimeException(e)
        }
    }
}