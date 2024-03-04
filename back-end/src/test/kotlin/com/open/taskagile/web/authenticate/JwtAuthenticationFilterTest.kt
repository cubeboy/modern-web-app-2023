package com.open.taskagile.web.authenticate

import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.verify
import net.bytebuddy.matcher.ElementMatchers.returns
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono
import reactor.kotlin.test.test
import reactor.test.StepVerifier
import reactor.test.StepVerifierOptions
import java.time.Duration

@ExtendWith(MockKExtension::class)
class JwtAuthenticationFilterTest {
  @MockK(relaxed = true)
  private lateinit var tokenProvider:JwtTokenProvider
  @MockK(relaxed = true)
  private lateinit var exchange:ServerWebExchange
  @MockK(relaxed = true)
  private lateinit var chain:WebFilterChain

  private lateinit var authFilter:JwtTokenAuthenticationFilter

  @BeforeEach
  fun setup() {
    this.authFilter = JwtTokenAuthenticationFilter(this.tokenProvider)
  }

  @Test
  fun whenValidTokenThenSuccess() {
    val usernamePasswordToken = UsernamePasswordAuthenticationToken(
      "test",
      "password",
      AuthorityUtils.createAuthorityList("ROLE_USER")
    )

    every { exchange.request.headers.getFirst(HttpHeaders.AUTHORIZATION) } returns "Bearer atesttoken"
    every { tokenProvider.validateToken(any()) } returns true
    every { tokenProvider.getAuthentication(any()) } returns usernamePasswordToken
    every { chain.filter(exchange) } returns Mono.empty()

    authFilter.filter(exchange, chain).test().verifyComplete()

    verify(exactly = 1) { tokenProvider.getAuthentication("atesttoken") }
    verify(exactly = 1) { chain.filter(exchange) }
  }

  @Test
  fun whenWithNoTokenThenFail() {
    every { exchange.request.headers.getFirst(HttpHeaders.AUTHORIZATION) } returns null
    every { chain.filter(exchange) } returns Mono.empty()

    authFilter.filter(exchange, chain).test().verifyComplete()

    verify(exactly = 0) { tokenProvider.getAuthentication(any()) }
    verify(exactly = 1) { chain.filter(exchange) }
  }

  @Test
  fun whenWithInvalidTokenThenFail() {
    every { exchange.request.headers.getFirst(HttpHeaders.AUTHORIZATION) } returns "Bearer atesttoken"
    every { tokenProvider.validateToken(any()) } returns false
    every { chain.filter(exchange) } returns Mono.empty()

    authFilter.filter(exchange, chain).test().verifyComplete()

    verify(exactly = 0) { tokenProvider.getAuthentication(any()) }
    verify(exactly = 1) { chain.filter(exchange) }
  }
}
