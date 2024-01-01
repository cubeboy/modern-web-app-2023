package com.open.taskagile.web.payload

import com.open.taskagile.web.response.*
import jakarta.validation.constraints.Pattern

class RegistrationPayload(
  usernameParam:String,
  emailAddressParam:String,
  passwordParam:String
) {
  @Pattern(regexp="^[a-zA-Z0-9]{4,20}$",
    message = REGISTER_USERNAME_INVALID)
  val username:String = usernameParam.trim()
  @Pattern(regexp = "^(?=.{6,100}$)[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
    message = REGISTER_EMAIL_ADDRESS_INVALID )
  val emailAddress:String = emailAddressParam.trim()
  @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()-_=+\\[\\]{}|;:'\",.<>?/])[A-Za-z\\d!@#$%^&*()-_=+\\[\\]{}|;:'\",.<>?/]{6,128}$",
    message = REGISTER_PASSWORD_INVALID)
  val password:String = passwordParam.trim()
}
