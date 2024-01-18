package com.open.taskagile.infra.repository.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table(name = "users")
class Users(
  @field:Id
  @field:Column("user_id")
  val id:Long? = null,
  @field:Column("username")
  val username:String,
  @field:Column("first_name")
  val firstName:String = "",
  @field:Column("last_name")
  val lastName:String = "",
  @field:Column("email_address")
  val emailAddress:String,
  @field:Column("password")
  val password:String
)
