package com.open.taskagile.web.payload

import jakarta.validation.constraints.NotBlank

class AuthenticationPayload(
  @field:NotBlank
  val username:String,
  @field:NotBlank
  val password:String
)
