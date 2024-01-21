@file:Suppress("NonAsciiCharacters")

package com.open.taskagile.infra.repository.r2dbc

import com.open.taskagile.config.TestR2dbcConfig
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import reactor.kotlin.test.test

@Suppress("UnusedEquals")
@DataR2dbcTest
@EnableR2dbcRepositories
@ContextConfiguration(classes = [TestR2dbcConfig::class])
@ActiveProfiles("test")
class R2DbcUserRepositoryTests {
  @Autowired
  lateinit var userRepository: R2DbcUserRepository

  @Test
  fun `username 으로 user 찾기 성공`() {
    val user = userRepository.findByUsernameOrEmailAddress("username1", "any-email-address")
    user.test()
      .expectSubscription()
      .expectNextCount(1)
      .verifyComplete()
  }

  @Test
  fun `emailAddress 으로 user 찾기 성공`() {
    val user = userRepository.findByUsernameOrEmailAddress("anyUsername", "username2@taskagile.com")
    user.test()
      .expectSubscription()
      .assertNext {
        it.username == "username2" && it.emailAddress == "username2@taskagile.com"
      }
      .verifyComplete()
  }

  @Test
  fun `등록되지 않은 username & emailAddress`() {
    val user = userRepository.findByUsernameOrEmailAddress("anyUsername", "ayn-email-address")
    user.test()
      .expectSubscription()
      .expectNextCount(0)
      .verifyComplete()
  }

  @Test
  fun `정상 등록 후 user_id 반환`() {
    userRepository.register("testuser", "testuser@taskagiel.com", "P@ssword123")
      .test()
      .expectSubscription()
      .assertNext {
        it == 4L
      }
      .verifyComplete()
  }
}
