package com.open.taskagile.infra.repository.mariadb

import com.infobip.spring.data.r2dbc.QuerydslR2dbcRepository
import com.open.taskagile.infra.repository.UserRepository
import com.open.taskagile.infra.repository.entity.QUsers.users
import com.open.taskagile.infra.repository.entity.Users
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty

@Repository
class MariaUserRepository(
  private val userRepository: IUserRepository
): UserRepository  {
  override fun register(username: String, emailAddress: String, password: String): Mono<Long> {
    val newUser = Users(username = username, emailAddress = emailAddress, password = password)
    return userRepository.save(newUser)
      .map {
        it.id ?: throw IllegalStateException("Register User Failed.")
      }
  }

  override fun findByUsernameOrEmailAddress(username: String, emailAddress: String): Mono<Users> {
    val predicate = users.username.eq(username).or(users.emailAddress.eq(emailAddress))
    return userRepository.query {
      it.select(users)
        .from(users)
        .where(predicate)
    }.first().switchIfEmpty { Mono.empty() }
  }
}
