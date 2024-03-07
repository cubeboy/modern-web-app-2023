package com.open.taskagile.web.authenticate

import com.open.taskagile.configuration.ApplicationConfiguration
import com.open.taskagile.configuration.JwtProperties
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import java.nio.charset.StandardCharsets
import java.util.*
import java.util.stream.Collectors

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ContextConfiguration(classes = [ApplicationConfiguration::class])
@ConfigurationPropertiesScan("com.open.taskagile.configuration.JwtProperties")
@ActiveProfiles("test")
class JwtTokenProviderTests {
  val log: Logger = LoggerFactory.getLogger(JwtTokenProviderTests::class.java)
  companion object {
    const val TEST_USER = "user"
    const val TEST_ROLE_NAME = "ROLE_USER"
  }

  @Autowired
  lateinit var jwtProperties: JwtProperties

  lateinit var tokenProvider:JwtTokenProvider

  @BeforeEach
  fun setup() {
    assertNotNull(jwtProperties)
    this.tokenProvider = JwtTokenProvider(jwtProperties)
    assertNotNull(this.tokenProvider)
  }

  @Test
  fun generateAndParseToken() {
    val token = generateToken(TEST_USER, TEST_ROLE_NAME)
    log.debug("generated jwt token :: {}", token)
    val auth = this.tokenProvider.getAuthentication(token)
    val principal:UserDetails = auth.principal as UserDetails
    assertEquals(principal.username, TEST_USER)
    assertTrue(principal.authorities.stream()
      .map { it.authority }
      .collect(Collectors.toList())
      .contains(TEST_ROLE_NAME))
  }

  @Test
  fun generateAndParseToken_withoutRoles() {
    val token = generateToken(TEST_USER)
    log.debug("generated jwt token :: {}", token)
    val auth = this.tokenProvider.getAuthentication(token)
    val principal = auth.principal as UserDetails
    assertEquals(principal.username, TEST_USER)
    assertTrue(principal.authorities.isEmpty())
  }

  @Test
  fun parseTokenException() {
    val invalidToken = "anunknowtokencannotbeparsedbyjwtprovider"
    assertThrows<JwtException> { this.tokenProvider.getAuthentication(invalidToken) }
  }

  @Test
  fun validateTokenException_failed() {
    val invalidToken = "anunknowtokencannotbeparsedbyjwtprovider"
    assertFalse(this.tokenProvider.validateToken(invalidToken))
  }

  @Test
  fun validateExpirationDate() {
    val secret = Base64.getEncoder().encodeToString(this.jwtProperties.secretKey.toByteArray())
    val secretKey = Keys.hmacShaKeyFor(secret.toByteArray(StandardCharsets.UTF_8))

    val claims = Jwts.claims().subject(TEST_USER).build()
    val now = Date()
    val validity = Date(now.time - 1)
    val expiredToken = Jwts.builder()
      .claims(claims)
      .issuedAt(now)
      .expiration(validity)
      .signWith(secretKey, Jwts.SIG.HS256)
      .compact()

    assertFalse(this.tokenProvider.validateToken(expiredToken))
  }

  @Test
  fun validateToken() {
    val token = generateToken(TEST_USER, TEST_ROLE_NAME)
    assertTrue(this.tokenProvider.validateToken(token))
  }

  private fun generateToken(username:String, vararg roles:String): String {
    val authorities =
      AuthorityUtils.createAuthorityList(*roles)
    val principal = User(username, "password", authorities)
    val usernamePasswordAuthenticationToken =
      UsernamePasswordAuthenticationToken(principal, null, authorities)

    return this.tokenProvider.createToken(usernamePasswordAuthenticationToken)
  }
}
