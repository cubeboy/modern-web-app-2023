package com.open.taskagile.web.api

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.reactive.server.WebTestClient

@WebFluxTest(controllers = [CurrentUserController::class])
class CurrentUserControllerTest {
  @Autowired
  lateinit var client:WebTestClient

  @Test
  @WithMockUser()
  fun getCurrentUserSuccess() {
    this.client.get()
      .uri("/api/me")
      .exchange()
      .expectStatus().isOk
      .expectBody()
      .jsonPath("$.name").isEqualTo("user")
      .jsonPath("$.roles").isArray()
      .jsonPath("$.roles[0]").isEqualTo("ROLE_USER")
  }
}
