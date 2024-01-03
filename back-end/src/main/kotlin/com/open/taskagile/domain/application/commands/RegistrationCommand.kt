package com.open.taskagile.domain.application.commands

data class RegistrationCommand(
  val username:String,
  val emailAddress:String,
  val password:String
)
