package com.open.taskagile.config

import io.r2dbc.spi.ConnectionFactory
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.core.io.ClassPathResource
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.r2dbc.connection.R2dbcTransactionManager
import org.springframework.r2dbc.connection.init.CompositeDatabasePopulator
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator
import org.springframework.transaction.ReactiveTransactionManager

@TestConfiguration
@ComponentScan(basePackages = ["com.open.taskagile.infra.repository.r2dbc"])
class TestR2dbcConfig {
  @Bean
  fun initializer(connectionFactory: ConnectionFactory?): ConnectionFactoryInitializer {
    val initializer = ConnectionFactoryInitializer()
    initializer.setConnectionFactory(connectionFactory!!)
    val populator = CompositeDatabasePopulator()
    populator.addPopulators(ResourceDatabasePopulator(ClassPathResource("user/initRepository.sql")))
    initializer.setDatabasePopulator(populator)
    return initializer
  }

  @Bean
  fun reactiveTransactionManager(connectionFactory: ConnectionFactory): ReactiveTransactionManager {
    return R2dbcTransactionManager(connectionFactory)
  }

  @Bean
  fun entityTemplate(connectionFactory: ConnectionFactory): R2dbcEntityTemplate {
    return R2dbcEntityTemplate(connectionFactory)
  }
}
