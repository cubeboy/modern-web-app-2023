package com.open.taskagile.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.web.server.SecurityWebFilterChain

@Configuration
@EnableWebFluxSecurity
class SecurityConfiguration {
  companion object {
    val PUBLIC = arrayOf("/", "/error", "/login", "/api/registrations")
  }

  @Bean
  fun springWebFilterChain(http:ServerHttpSecurity): SecurityWebFilterChain {
    return http
      .authorizeExchange { exchange ->
        exchange
          .pathMatchers(*PUBLIC).permitAll()
          .anyExchange().permitAll()
      }
      .csrf{ it.disable() }
      .build()
  }
}
