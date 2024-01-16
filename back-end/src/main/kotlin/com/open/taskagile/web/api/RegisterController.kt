package com.open.taskagile.web.api

import com.open.taskagile.application.UserService
import com.open.taskagile.application.domain.user.RegistrationException
import com.open.taskagile.web.payload.RegistrationPayload
import com.open.taskagile.web.response.ApiResponse
import com.open.taskagile.web.response.REGISTER_SUCCESS
import com.open.taskagile.web.response.Responsor
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

@RestController
class RegisterController(val userService: UserService) {
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
