package com.open.taskagile.web.api

import com.open.taskagile.web.authenticate.JwtTokenProvider
import com.open.taskagile.web.payload.AuthenticationPayload
import com.open.taskagile.web.response.ApiResponse
import com.open.taskagile.web.response.Responsor
import jakarta.validation.Valid
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping(AUTH)
@Validated
class AuthController(
  val tokenProvider:JwtTokenProvider,
  val authenticationManager:ReactiveAuthenticationManager
) {
  @PostMapping(LOGIN)
  fun login(@Valid @RequestBody  authRequest:AuthenticationPayload): Mono<ResponseEntity<ApiResponse>> {
    return authenticationManager
      .authenticate(UsernamePasswordAuthenticationToken(authRequest.username, authRequest.password))
      .map { tokenProvider.createToken(it) }
      .map {
        val httpHeaders = HttpHeaders()
        httpHeaders.add(HttpHeaders.AUTHORIZATION, "Bearer $it")
        val response = ApiResponse.createApiResult(HttpStatus.OK, "success")
        response.add("access_token", it)
        Responsor.ok(response, httpHeaders)
      }
  }
}
