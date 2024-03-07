package com.open.taskagile.configuration

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ContextConfiguration(classes = [ApplicationConfiguration::class])
@ConfigurationPropertiesScan("com.open.taskagile.configuration.JwtProperties")
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
