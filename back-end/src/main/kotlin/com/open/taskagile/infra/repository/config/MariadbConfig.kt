package com.open.taskagile.infra.repository.config

import com.infobip.spring.data.r2dbc.EnableQuerydslR2dbcRepositories
import com.querydsl.sql.MySQLTemplates
import com.querydsl.sql.SQLTemplates
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.annotation.EnableTransactionManagement

@Configuration
@EnableTransactionManagement
@EnableQuerydslR2dbcRepositories
class MariadbConfig {
  @Bean
  fun sqlTemplates(): SQLTemplates {
    return MySQLTemplates()
  }
}
