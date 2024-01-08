package com.open.taskagile.domain.application.domain.user

import com.open.taskagile.domain.application.domain.user.model.User
import reactor.core.publisher.Mono

interface UserRepository {
  fun register(username:String, emailAddress:String, password:String): Mono<Long>
  fun findByUsernameOrEmailAddress(username:String, emailAddress:String): Mono<User>?
}
