package com.open.taskagile.application.commands

data class RegistrationCommand(
  val username:String,
  val emailAddress:String,
  val password:String
)
