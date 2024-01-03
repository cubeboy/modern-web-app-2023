package com.open.taskagile.web.api

import com.open.taskagile.web.payload.RegistrationPayload
import com.open.taskagile.web.response.ApiResponse
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

@RestController
class RegisterController {
  private final val log = LoggerFactory.getLogger(RegisterController::class.java)
  @PostMapping(REGISTER)
  fun register(
    @Valid @RequestBody payload: RegistrationPayload
  ): Mono<ResponseEntity<ApiResponse>> {
    return ResponseEntity.status(201).build<ApiResponse>().toMono()
  }
}
