package com.open.taskagile.configuration

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "jwt")
data class JwtProperties(
  val secretKey:String,
  val validityInMs:Long
)
