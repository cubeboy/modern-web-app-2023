package com.open.taskagile.infra.repository

import com.open.taskagile.infra.repository.entity.User
import reactor.core.publisher.Mono

interface UserRepository {
  fun register(username: String, emailAddress: String, password: String): Mono<Long>
  fun findByUsernameOrEmailAddress(username: String, emailAddress: String): Mono<User>
}
