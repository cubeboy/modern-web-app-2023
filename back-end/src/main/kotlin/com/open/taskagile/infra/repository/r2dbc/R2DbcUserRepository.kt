package com.open.taskagile.infra.repository.r2dbc

import com.open.taskagile.infra.repository.UserRepository
import com.open.taskagile.infra.repository.entity.Users
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.data.relational.core.query.Criteria
import org.springframework.data.relational.core.query.Query
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
class R2DbcUserRepository(
  private val query: R2dbcEntityTemplate
): UserRepository  {
  override fun register(username: String, emailAddress: String, password: String): Mono<Long> {
    val newUser = Users(username = username, emailAddress = emailAddress, password = password)
    return query.insert(newUser)
      .map {
        it.id ?: throw IllegalStateException("Register User Failed.")
      }
//    return userRepository.save(newUser)
//      .map {
//        it.id ?: throw IllegalStateException("Register User Failed.")
//      }
  }

  override fun findByUsernameOrEmailAddress(username: String, emailAddress: String): Mono<Users> {
    val criteria = Query.query(Criteria
      .where("username").`is`(username)
      .or("email_address").`is`(emailAddress))
    return query.selectOne(criteria, Users::class.java)
  }
}
