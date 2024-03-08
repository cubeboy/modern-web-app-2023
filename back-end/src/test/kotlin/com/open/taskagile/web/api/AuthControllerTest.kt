package com.open.taskagile.web.api

import com.open.taskagile.web.authenticate.JwtTokenProvider
import com.open.taskagile.web.payload.AuthenticationPayload
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration
import org.springframework.boot.autoconfigure.security.reactive.ReactiveUserDetailsServiceAutoConfiguration
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.kotlin.core.publisher.toMono

@ExtendWith(MockKExtension::class)
@WebFluxTest(
  controllers = [AuthController::class],
  excludeAutoConfiguration = [
    ReactiveUserDetailsServiceAutoConfiguration::class,
    ReactiveSecurityAutoConfiguration::class
  ]
)
class AuthControllerTest {
  @Autowired
  private lateinit var tokenProvider:JwtTokenProvider
  @Autowired
  private lateinit var authenticationManager: ReactiveAuthenticationManager

  @Autowired
  private lateinit var client: WebTestClient

  @TestConfiguration
  class TestConfig {
    @Bean
    fun tokenProvider() = mockk<JwtTokenProvider>()
    @Bean
    fun authenticationManager() = mockk<ReactiveAuthenticationManager>()
  }

  @Test
  fun whenUsernamePasswordLoginSuccess() {
    val usernamePasswordAuthenticationToken = UsernamePasswordAuthenticationToken(
      "test",
      "password",
      AuthorityUtils.createAuthorityList("ROLE_USER")
    )
    every { authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken::class)) } returns
      usernamePasswordAuthenticationToken.toMono()
    every { tokenProvider.createToken(any(Authentication::class)) } returns "atesttoken"

    val req = AuthenticationPayload("test", "password")
    client.post()
      .uri("/auth/login")
      .contentType(MediaType.APPLICATION_JSON)
      .bodyValue(req)
      .exchange()
      .expectStatus().isOk
      .expectHeader().valueEquals(HttpHeaders.AUTHORIZATION, "Bearer atesttoken")
      .expectBody().jsonPath("$.access_token").isEqualTo("atesttoken")

    verify(exactly = 1) { authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken::class)) }
    verify(exactly = 1) { tokenProvider.createToken(any(Authentication::class)) }
  }
}
