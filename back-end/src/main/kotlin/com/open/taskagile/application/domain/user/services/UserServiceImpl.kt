package com.open.taskagile.application.domain.user.services

import com.open.taskagile.application.UserService
import com.open.taskagile.application.commands.RegistrationCommand
import com.open.taskagile.application.domain.common.security.PasswordEncryptor
import com.open.taskagile.application.domain.user.RegistrationException
import com.open.taskagile.application.domain.user.events.UserRegisteredEvent
import com.open.taskagile.application.events.EventPublisher
import com.open.taskagile.infra.messages.UserNotificationType
import com.open.taskagile.infra.messages.UserNotifier
import com.open.taskagile.infra.repository.UserRepository
import com.open.taskagile.web.response.REGISTER_EMAIL_ADDRESS_EXISTS
import com.open.taskagile.web.response.REGISTER_USERNAME_EXISTS
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty
import reactor.kotlin.core.publisher.toMono

@Service
class UserServiceImpl(
  private val userRepository: UserRepository,
  private val eventPublisher: EventPublisher,
  private val userNotifier: UserNotifier,
  private val passwordEncryptor: PasswordEncryptor
): UserService {
  override fun register(command: RegistrationCommand): Mono<Long> {
    return command.toMono()
      .flatMap {
        userRepository.findByUsernameOrEmailAddress(command.username, command.emailAddress)
      }
      .flatMap {
        if(it.username == command.username) {
          Mono.error<Long>(RegistrationException(REGISTER_USERNAME_EXISTS))
        } else {
          Mono.error<Long>(RegistrationException(REGISTER_EMAIL_ADDRESS_EXISTS))
        }
      }
      .switchIfEmpty {
        passwordEncryptor.encrypt(command.password)
          .flatMap {
            userRepository.register(command.username, command.emailAddress, it)
          }
      }
      .map {
        val event = UserRegisteredEvent(this, it)
        eventPublisher.publish(event)
        it
      }.map {
        userNotifier.notify(it, UserNotificationType.USER_REGISTERED)
        it
      }
  }
}
