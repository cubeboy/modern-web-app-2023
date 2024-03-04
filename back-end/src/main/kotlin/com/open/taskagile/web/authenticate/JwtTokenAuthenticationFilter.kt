package com.open.taskagile.web.authenticate

import org.springframework.http.HttpHeaders
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.util.StringUtils
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers

class JwtTokenAuthenticationFilter(private val tokenProvider:JwtTokenProvider) : WebFilter {
  companion object {
    private const val HEADER_PREFIX = "Bearer "
  }
  override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
    val token = resolveToken(exchange.request)
    if(StringUtils.hasText(token) && tokenProvider.validateToken(token)) {
      return Mono.fromCallable { this.tokenProvider.getAuthentication(token) }
        .flatMap {
          chain.filter(exchange)
            .contextWrite(ReactiveSecurityContextHolder.withAuthentication(it))
        }
    }
    return chain.filter(exchange)
  }

  private fun resolveToken(request: ServerHttpRequest): String {
    val bearerToken = request.headers.getFirst(HttpHeaders.AUTHORIZATION) ?: ""
    if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(HEADER_PREFIX)) {
      return bearerToken.substring(HEADER_PREFIX.length)
    }
    return ""
  }
}
