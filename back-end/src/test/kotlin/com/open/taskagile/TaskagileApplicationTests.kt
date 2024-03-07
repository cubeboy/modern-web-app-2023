package com.open.taskagile

import com.open.taskagile.configuration.ApplicationConfiguration
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.test.context.ContextConfiguration

@SpringBootTest
@ContextConfiguration(classes = [ApplicationConfiguration::class])
@ConfigurationPropertiesScan("com.open.taskagile.configuration.JwtProperties")
class TaskagileApplicationTests {
  @Autowired
  lateinit var context:ApplicationContext

	@Test
	fun contextLoads() {
    Assertions.assertNotNull(context)
	}
}
