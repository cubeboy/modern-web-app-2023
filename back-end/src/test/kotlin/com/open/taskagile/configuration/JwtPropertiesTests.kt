package com.open.taskagile.configuration

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class JwtPropertiesTests {
  private val log = LoggerFactory.getLogger(JwtPropertiesTests::class.java)
  @Autowired
  lateinit var jwtProperties: JwtProperties

  @Test
  fun appStartInitJwtProperties() {
    val secretKey = System.getProperty("SECRET_KEY") ?: ""
    log.info("generated secretKey :: {}", secretKey)
    Assertions.assertEquals(secretKey, jwtProperties.secretKey)
    Assertions.assertEquals(36000L, jwtProperties.validityInMs)
  }
}
