package com.open.taskagile.web.api

import com.open.taskagile.web.payload.RegistrationPayload
import com.open.taskagile.web.response.*
import io.mockk.junit5.MockKExtension
import org.json.JSONObject
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient

@ExtendWith(MockKExtension::class)
@WebFluxTest(RegisterController::class)
class RegisterControllerTests {
  private val log = LoggerFactory.getLogger(RegisterControllerTests::class.java)
  @Autowired
  private lateinit var client:WebTestClient

  private val username = "testuser"
  private val emailAddress = "testuser@tasagile.com"
  private val password = "P@ssword123"

  @Test
  fun `잘못된 payload 오류`() {
    val payload = JSONObject()
    payload.put("username", null)
    payload.put("emailAddress", emailAddress)
    payload.put("password", password)

    client.post()
      .uri(REGISTER)
      .contentType(MediaType.APPLICATION_JSON)
      .bodyValue(payload.toString())
      .exchange()
      .expectStatus().isBadRequest
      .expectBody()
  }

  @Test
  fun `유효하지 않은 username 오류`() {
    val payload = RegistrationPayload("   ", emailAddress, password)
    client.post()
      .uri(REGISTER)
      .contentType(MediaType.APPLICATION_JSON)
      .bodyValue(payload)
      .exchange()
      .expectStatus().isEqualTo(409)
      .expectBody()
      .jsonPath("$.message").isEqualTo(REGISTER_USERNAME_INVALID)

    val shortUsername = RegistrationPayload("abc", emailAddress, password)
    client.post()
      .uri(REGISTER)
      .contentType(MediaType.APPLICATION_JSON)
      .bodyValue(shortUsername)
      .exchange()
      .expectStatus().isEqualTo(409)
      .expectBody()
      .jsonPath("$.message").isEqualTo(REGISTER_USERNAME_INVALID)
  }

  @Test
  fun `유효하지 않은 emalAddress 오류`() {
    val badEmailAccount = RegistrationPayload(username, "bad-account", password)
    client.post()
      .uri(REGISTER)
      .contentType(MediaType.APPLICATION_JSON)
      .bodyValue(badEmailAccount)
      .exchange()
      .expectStatus().isEqualTo(409)
      .expectBody()
      .jsonPath("$.message").isEqualTo(REGISTER_EMAIL_ADDRESS_INVALID)

    val badEmailDomain = RegistrationPayload(username, "testuser@bad-domain", password)
    client.post()
      .uri(REGISTER)
      .contentType(MediaType.APPLICATION_JSON)
      .bodyValue(badEmailDomain)
      .exchange()
      .expectStatus().isEqualTo(409)
      .expectBody()
      .jsonPath("$.message").isEqualTo(REGISTER_EMAIL_ADDRESS_INVALID)
  }

  @Test
  fun `유효하지 않은 password 오류`() {
    val shortPassword = RegistrationPayload(username, emailAddress, "abc12")
    client.post()
      .uri(REGISTER)
      .contentType(MediaType.APPLICATION_JSON)
      .bodyValue(shortPassword)
      .exchange()
      .expectStatus().isEqualTo(409)
      .expectBody()
      .jsonPath("$.message").isEqualTo(REGISTER_PASSWORD_INVALID)
  }
}
