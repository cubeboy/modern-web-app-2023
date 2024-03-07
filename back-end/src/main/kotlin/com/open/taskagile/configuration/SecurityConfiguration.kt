package com.open.taskagile.configuration

import com.open.taskagile.web.authenticate.JwtTokenAuthenticationFilter
import com.open.taskagile.web.authenticate.JwtTokenProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.SecurityWebFiltersOrder
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.web.server.SecurityWebFilterChain

@Configuration
@EnableWebFluxSecurity
class SecurityConfiguration {
  companion object {
    val PUBLIC = arrayOf("/", "/error", "/login", "/api/registrations")
  }

  @Bean
  fun springWebFilterChain(http:ServerHttpSecurity, tokenProvider: JwtTokenProvider): SecurityWebFilterChain {
    return http
      .csrf{ it.disable() }
      .httpBasic { it.disable() }
      .authorizeExchange { exchange ->
        exchange
          .pathMatchers(*PUBLIC).permitAll()
          .anyExchange().permitAll()
      }
      .addFilterAt(JwtTokenAuthenticationFilter(tokenProvider), SecurityWebFiltersOrder.HTTP_BASIC)
      .build()
  }
}
