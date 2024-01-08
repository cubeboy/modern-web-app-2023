package com.open.taskagile.domain.application.domain.user.services

import com.open.taskagile.domain.application.UserService
import com.open.taskagile.domain.application.commands.RegistrationCommand
import com.open.taskagile.domain.application.common.messages.NotificationType
import com.open.taskagile.domain.application.common.messages.UserNotifier
import com.open.taskagile.domain.application.domain.user.RegistrationException
import com.open.taskagile.domain.application.domain.user.UserRepository
import com.open.taskagile.domain.application.domain.user.events.UserRegisteredEvent
import com.open.taskagile.domain.application.events.DomainEvent
import com.open.taskagile.domain.application.events.EventPublisher
import com.open.taskagile.web.response.REGISTER_EMAIL_ADDRESS_EXISTS
import com.open.taskagile.web.response.REGISTER_USERNAME_EXISTS
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty
import reactor.kotlin.core.publisher.toMono

@Service
class RegistrationServiceImpl(
  val userRepository: UserRepository,
  val eventPublisher: EventPublisher,
  val userNotifier: UserNotifier
) :UserService {
  override fun register(command: RegistrationCommand): Mono<Long> {
    return command.toMono()
      .flatMap {
        userRepository.findByUsernameOrEmailAddress(command.username, command.emailAddress)
      }
      .flatMap {
        if(it.username.equals(command.username)) {
          Mono.error<Long>(RegistrationException(REGISTER_USERNAME_EXISTS))
        } else {
          Mono.error<Long>(RegistrationException(REGISTER_EMAIL_ADDRESS_EXISTS))
        }
      }
      .switchIfEmpty {
        userRepository.register(command.username, command.emailAddress, command.password)
      }
      .map {
        val event = UserRegisteredEvent(it)
        eventPublisher.publish(event)
        it
      }
      .map {
        userNotifier.notify(it, NotificationType.USER_REGISTERED)
        it
      }
  }
}
