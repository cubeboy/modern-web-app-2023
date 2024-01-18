package com.open.taskagile.infra.repository.mariadb

import com.querydsl.sql.MySQLTemplates
import com.querydsl.sql.SQLTemplates
import io.r2dbc.spi.ConnectionFactory
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.core.io.ClassPathResource
import org.springframework.r2dbc.connection.R2dbcTransactionManager
import org.springframework.r2dbc.connection.init.CompositeDatabasePopulator
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator
import org.springframework.transaction.ReactiveTransactionManager

@TestConfiguration
@ComponentScan(basePackages = ["com.open.taskagile.infra.repository.mariadb"])
class TestMariadbConfig {
  @Bean
  fun sqlTemplates(): SQLTemplates {
    return MySQLTemplates()
  }

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
}
