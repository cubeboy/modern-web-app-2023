package com.open.taskagile.application

import com.open.taskagile.application.commands.RegistrationCommand
import reactor.core.publisher.Mono

interface UserService {
  fun register(command: RegistrationCommand): Mono<Long>
}
