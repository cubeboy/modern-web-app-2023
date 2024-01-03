package com.open.taskagile.domain.application

import com.open.taskagile.domain.application.commands.RegistrationCommand
import reactor.core.publisher.Mono

interface UserService {
  fun register(command: RegistrationCommand): Mono<Long>
}
