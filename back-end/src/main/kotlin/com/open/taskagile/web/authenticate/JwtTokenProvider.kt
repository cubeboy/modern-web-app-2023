package com.open.taskagile.web.authenticate

import com.open.taskagile.configuration.JwtProperties
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.userdetails.User
import java.nio.charset.StandardCharsets
import java.util.*
import java.util.stream.Collectors.joining
import javax.crypto.SecretKey

class JwtTokenProvider(private val jwtProperties: JwtProperties) {
  private val log = LoggerFactory.getLogger(JwtTokenProvider::class.java)
  companion object {
    val AUTHORITIES_KEYS = "role"
  }

  private var secretKey:SecretKey
  init {
    val secret = Base64.getEncoder()
      .encodeToString(jwtProperties.secretKey.toByteArray())
    this.secretKey = Keys.hmacShaKeyFor(
      secret.toByteArray(StandardCharsets.UTF_8)
    )
  }

  fun createToken(authentication: Authentication): String {
    val username = authentication.name
    val authorities = authentication.authorities
    val claimBuilder = Jwts.claims().subject(username)
    if(!authorities.isEmpty()) {
      claimBuilder.add(AUTHORITIES_KEYS, authorities.stream()
        .map {
          it.authority
        }.collect(joining(","))
      )
    }
    val claims = claimBuilder.build()
    val now = Date()
    val validity = Date(now.time + jwtProperties.validityInMs)

    return Jwts.builder()
      .claims(claims)
      .issuedAt(now)
      .expiration(validity)
      .signWith(this.secretKey, Jwts.SIG.HS256)
      .compact()
  }

  fun validateToken(token:String): Boolean {
    try {
      val claims = Jwts.parser().verifyWith(this.secretKey)
        .build().parseSignedClaims(token)
      log.debug("expiration date: {}", claims.payload.expiration)
      return true
    } catch (e: Exception) {
      when(e) {
        is JwtException,
        is IllegalArgumentException -> {
          log.error("Invalid JWT token: {}", e.message)
          log.trace("Invalid JWT token trace.", e)
        }
        else -> throw e
      }
    }
    return false
  }
  fun getAuthentication(token: String): Authentication {
    val claims = Jwts.parser().verifyWith(this.secretKey).build()
      .parseSignedClaims(token).payload
    val authoritiesClaim = claims[(AUTHORITIES_KEYS)]

    val authorities = if(authoritiesClaim == null)
      { AuthorityUtils.NO_AUTHORITIES }
      else { AuthorityUtils.commaSeparatedStringToAuthorityList(authoritiesClaim.toString()) }
    val principal = User(claims.subject, "", authorities)
    return UsernamePasswordAuthenticationToken(principal, token, authorities)
  }
}
