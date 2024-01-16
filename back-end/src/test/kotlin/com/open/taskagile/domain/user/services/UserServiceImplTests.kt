@file:Suppress("NonAsciiCharacters")

package com.open.taskagile.domain.user.services

import com.open.taskagile.application.UserService
import com.open.taskagile.application.commands.RegistrationCommand
import com.open.taskagile.application.domain.user.RegistrationException
import com.open.taskagile.application.domain.user.services.UserServiceImpl
import com.open.taskagile.application.events.EventPublisher
import com.open.taskagile.infra.messages.UserNotificationType
import com.open.taskagile.infra.messages.UserNotifier
import com.open.taskagile.infra.repository.UserRepository
import com.open.taskagile.infra.repository.entity.User
import com.open.taskagile.web.response.REGISTER_EMAIL_ADDRESS_EXISTS
import com.open.taskagile.web.response.REGISTER_USERNAME_EXISTS
import io.mockk.*
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import reactor.kotlin.test.test

@ExtendWith(MockKExtension::class)
class UserServiceImplTests {
  private lateinit var userService: UserService

  @MockK(relaxed = true)
  private lateinit var userRepository: UserRepository
  @MockK(relaxed = true)
  private lateinit var eventPublisher: EventPublisher
  @MockK(relaxed = true)
  private lateinit var userNotifier: UserNotifier

  private val username = "testuser"
  private val emailAddress = "testuser@taskagile.com"
  private val password = "P@ssword123"

  @BeforeEach
  fun setUp() {
    MockKAnnotations.init(this)
    userService = UserServiceImpl(userRepository, eventPublisher, userNotifier)
  }

  @Test
  fun `중복된 Username 등록 오류`() {
    val command = RegistrationCommand("existsUsername", emailAddress, password)
    every { userRepository.findByUsernameOrEmailAddress(command.username, command.emailAddress) } returns
      User(command.username, "otherUser@taskagile.com").toMono()
    val mono = userService.register(command)
    mono.test()
      .expectSubscription()
      .expectErrorMatches {
        it is RegistrationException &&
          it.message.equals(REGISTER_USERNAME_EXISTS)
      }
      .verify()

    verify(exactly = 0) { userRepository.register(any(), any(), any()) }
    verify(exactly = 0) { eventPublisher.publish(any()) }
    verify(exactly = 0) { userNotifier.notify(any(), UserNotificationType.USER_REGISTERED) }
  }

  @Test
  fun `중복된 EmailAddress 등록 오류`() {
    val command = RegistrationCommand(username, "existsUser@taskagile.com", password)
    every { userRepository.findByUsernameOrEmailAddress(username, command.emailAddress) } returns
      User("otherUsername", command.emailAddress).toMono()

    val id = userService.register(command)
    id.test()
      .expectSubscription()
      .expectErrorMatches {
        it is RegistrationException &&
          it.message.equals(REGISTER_EMAIL_ADDRESS_EXISTS)
      }
      .verify()

    verify(exactly = 0) { userRepository.register(any(), any(), any()) }
    verify(exactly = 0) { eventPublisher.publish(any()) }
    verify(exactly = 0) { userNotifier.notify(any(), UserNotificationType.USER_REGISTERED) }
  }

  @Test
  fun `사용자 등록 성공`() {
    val userId = 999L
    val command = RegistrationCommand(username, emailAddress, password)

    every { userRepository.findByUsernameOrEmailAddress(command.username, command.emailAddress) } returns
      Mono.empty()
    every { userRepository.register(command.username, command.emailAddress, command.password)} returns
      userId.toMono()
    val id = userService.register(command)
    id.test()
      .expectSubscription()
      .expectNext(userId)
      .verifyComplete()

    verifySequence {
      userRepository.findByUsernameOrEmailAddress(username, emailAddress)
      userRepository.register(username, emailAddress, password)
      eventPublisher.publish(any())
      userNotifier.notify(userId, UserNotificationType.USER_REGISTERED)
    }
  }
}
