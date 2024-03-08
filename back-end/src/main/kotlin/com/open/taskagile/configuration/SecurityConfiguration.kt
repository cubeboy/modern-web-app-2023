package com.open.taskagile.configuration

import com.open.taskagile.infra.repository.UserRepository
import com.open.taskagile.web.authenticate.JwtTokenAuthenticationFilter
import com.open.taskagile.web.authenticate.JwtTokenProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager
import org.springframework.security.authorization.AuthorizationDecision
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.SecurityWebFiltersOrder
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.core.userdetails.User
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.authorization.AuthorizationContext
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository
import reactor.core.publisher.Mono

@Configuration
@EnableWebFluxSecurity
class SecurityConfiguration {
  companion object {
    val PUBLIC = arrayOf("/", "/error", "/login", "/api/registrations")
  }

  @Bean
  fun springWebFilterChain(
    http:ServerHttpSecurity,
    tokenProvider: JwtTokenProvider,
    reactiveAuthenticationManager: ReactiveAuthenticationManager
  ): SecurityWebFilterChain {
    return http
      .csrf{ it.disable() }
      .httpBasic { it.disable() }
      .authenticationManager(reactiveAuthenticationManager)
      .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
      .authorizeExchange { exchange ->
        exchange
          .pathMatchers(*PUBLIC).permitAll()
          .pathMatchers("/api/me").authenticated()
          .pathMatchers("/api/users/{user}/**").access(this::currentUserMatcherPath)
          .anyExchange().authenticated()
      }
      .addFilterAt(JwtTokenAuthenticationFilter(tokenProvider), SecurityWebFiltersOrder.HTTP_BASIC)
      .build()
  }

  private fun currentUserMatcherPath(
    authentication: Mono<Authentication>,
    context:AuthorizationContext
  ): Mono<AuthorizationDecision> {
    return authentication
      .map { context.variables["user"]?.equals(it.name) ?: false }
      .map { AuthorizationDecision(it) }
  }

  @Bean
  fun userDetailsService(users:UserRepository): ReactiveUserDetailsService {
    return ReactiveUserDetailsService { username ->
      users.findByUsernameOrEmailAddress(username, "")
        .map {
          User
            .withUsername(it.username)
            .password(it.password)
            .build()
        }
    }
  }

  @Bean
  fun reactiveAuthenticationManager(
    userDetailsService: ReactiveUserDetailsService,
    passwordEncoder:PasswordEncoder
  ): ReactiveAuthenticationManager {
    val authenticationManager = UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService)
    authenticationManager.setPasswordEncoder(passwordEncoder)
    return authenticationManager
  }

  @Bean
  fun passwordEncoder(): PasswordEncoder {
    return BCryptPasswordEncoder()
  }
}
