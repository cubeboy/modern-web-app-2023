package com.open.taskagile.infra.repository.config

import io.r2dbc.spi.ConnectionFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories
import org.springframework.r2dbc.connection.R2dbcTransactionManager
import org.springframework.transaction.ReactiveTransactionManager

@Configuration
@EnableR2dbcRepositories
class R2dbcConfig {
  @Bean
  fun transactionManager(connectionFactory: ConnectionFactory): ReactiveTransactionManager {
    return R2dbcTransactionManager(connectionFactory)
  }

  @Bean
  fun entityTemplate(connectionFactory: ConnectionFactory): R2dbcEntityTemplate {
    return R2dbcEntityTemplate(connectionFactory)
  }
}
