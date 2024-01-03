package com.open.taskagile.web.api

import com.open.taskagile.domain.application.UserService
import com.open.taskagile.domain.application.domain.user.RegistrationException
import com.open.taskagile.web.payload.RegistrationPayload
import com.open.taskagile.web.response.*
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.verify
import org.json.JSONObject
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec
import reactor.core.publisher.Mono

@ExtendWith(MockKExtension::class)
@WebFluxTest(RegisterController::class)
class RegisterControllerTests {
  private val log = LoggerFactory.getLogger(RegisterControllerTests::class.java)
  @Autowired
  private lateinit var client:WebTestClient

  @Autowired
  lateinit var userService: UserService

  private val username = "testuser"
  private val emailAddress = "testuser@tasagile.com"
  private val password = "P@ssword123"

  @TestConfiguration
  class TestConfig {
    @Bean
    fun userService() = mockk<UserService>()
  }

  @BeforeEach
  fun setUp() = MockKAnnotations.init(this)

  @Test
  fun `잘못된 payload 오류`() {
    val payload = JSONObject()
    payload.put("username", null)
    payload.put("emailAddress", emailAddress)
    payload.put("password", password)

    client.postCall(payload.toString())
      .expectStatus().isBadRequest
      .expectBody()
  }

  @Test
  fun `유효하지 않은 username 오류`() {
    val payload = RegistrationPayload("   ", emailAddress, password)
    client.postCall(payload)
      .expectStatus().isEqualTo(409)
      .expectBody()
      .jsonPath("$.message").isEqualTo(REGISTER_USERNAME_INVALID)

    val shortUsername = RegistrationPayload("abc", emailAddress, password)
    client.postCall(shortUsername)
      .expectStatus().isEqualTo(409)
      .expectBody()
      .jsonPath("$.message").isEqualTo(REGISTER_USERNAME_INVALID)
  }

  @Test
  fun `유효하지 않은 emailAddress 오류`() {
    val badEmailAccount = RegistrationPayload(username, "bad-account", password)
    client.postCall(badEmailAccount)
      .expectStatus().isEqualTo(409)
      .expectBody()
      .jsonPath("$.message").isEqualTo(REGISTER_EMAIL_ADDRESS_INVALID)

    val badEmailDomain = RegistrationPayload(username, "testuser@bad-domain", password)
    client.postCall(badEmailDomain)
      .expectStatus().isEqualTo(409)
      .expectBody()
      .jsonPath("$.message").isEqualTo(REGISTER_EMAIL_ADDRESS_INVALID)
  }

  @Test
  fun `유효하지 않은 password 오류`() {
    val shortPassword = RegistrationPayload(username, emailAddress, "abc12")
    client.postCall(shortPassword)
      .expectStatus().isEqualTo(409)
      .expectBody()
      .jsonPath("$.message").isEqualTo(REGISTER_PASSWORD_INVALID)
  }

  @Test
  fun `기 등록된 username 오류`() {
    val existsUsername = RegistrationPayload("existsUsername", emailAddress, password)
    every { userService.register(existsUsername.toCommand()) } returns
      Mono.error(RegistrationException(REGISTER_USERNAME_EXISTS))

    client.postCall(existsUsername)
      .expectStatus().isEqualTo(409)
      .expectBody()
      .jsonPath("$.message").isEqualTo(REGISTER_USERNAME_EXISTS)
    verify(exactly = 1) { userService.register(existsUsername.toCommand()) }
  }

  @Test
  fun `기 등록된 emailAddress 오류`() {
    val existsUsername = RegistrationPayload(username, "existsUser@taskagile.com", password)
    every { userService.register(existsUsername.toCommand()) } returns
      Mono.error(RegistrationException(REGISTER_EMAIL_ADDRESS_EXISTS))

    client.postCall(existsUsername)
      .expectStatus().isEqualTo(409)
      .expectBody()
      .jsonPath("$.message").isEqualTo(REGISTER_EMAIL_ADDRESS_EXISTS)
    verify(exactly = 1) { userService.register(existsUsername.toCommand()) }
  }

  @Test
  fun `사용자 정보 정상 등록`() {
    val payload = RegistrationPayload(username, emailAddress, password)
    every { userService.register(payload.toCommand()) } returns Mono.just(100)

    client.postCall(payload)
      .expectStatus().isEqualTo(201)
      .expectBody()
      .jsonPath("$.id").isEqualTo(100)
      .jsonPath("$.message").isEqualTo(REGISTER_SUCCESS)
  }

  fun WebTestClient.postCall(payload: Any): ResponseSpec {
    return this.post()
      .uri(REGISTER)
      .contentType(MediaType.APPLICATION_JSON)
      .bodyValue(payload)
      .exchange()
  }
}
