package com.open.taskagile.web.api

import com.open.taskagile.domain.application.UserService
import com.open.taskagile.domain.application.domain.user.RegistrationException
import com.open.taskagile.web.payload.RegistrationPayload
import com.open.taskagile.web.response.ApiResponse
import com.open.taskagile.web.response.REGISTER_SUCCESS
import com.open.taskagile.web.response.Responsor
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

@RestController
class RegisterController(val userService: UserService) {
  private final val log = LoggerFactory.getLogger(RegisterController::class.java)
  @PostMapping(REGISTER)
  fun register(
    @Valid @RequestBody payload: RegistrationPayload
  ): Mono<ResponseEntity<ApiResponse>> {
    return payload.toMono().map {
      payload.toCommand()
    }.flatMap {
      userService.register(it)
    }.map {
      Responsor.createSuccess(it, REGISTER_SUCCESS)
    }.onErrorResume(RegistrationException::class.java) {
      Responsor.responseConflict(it.message!!).toMono()
    }
  }
}
