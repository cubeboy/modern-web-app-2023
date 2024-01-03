package com.open.taskagile.web

import com.open.taskagile.web.response.ApiResponse
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.support.WebExchangeBindException
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

@ControllerAdvice
class GlobalExceptionHandler {
  private final val log = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)

  @ExceptionHandler(WebExchangeBindException::class)
  fun handleWebExchangeBindException(ex: WebExchangeBindException)
    : Mono<ResponseEntity<ApiResponse>> {
    log.debug("GlobalExceptionHandler::handleWebExchangeBindException")
    val message = ex.bindingResult.allErrors.stream().map { it.defaultMessage }.toList()
      .joinToString("\n")
    return ResponseEntity
      .status(HttpStatus.CONFLICT)
      .body(ApiResponse.createApiResult(HttpStatus.CONFLICT, message)).toMono()
  }
}
