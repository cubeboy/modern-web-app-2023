@file:Suppress("SpellCheckingInspection")

package com.open.taskagile.web.payload

import jakarta.validation.Validation
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

import com.open.taskagile.web.response.*
import net.bytebuddy.utility.RandomString

@Suppress("NonAsciiCharacters")
class RegistrationPayloadTests {
  private val validator = Validation.buildDefaultValidatorFactory().validator
  private val username = "testuser"
  private val emailAddress = "testuser@taskagile.com"
  private val password = "P@sword12"

  @Test
  fun `username 은 필수입력, 영문숫자만 허용,  최소 4글자 이상, 최대 20자 이하`() {
    val emptyUsername = RegistrationPayload("  ", emailAddress, password)
    val emptyViolations = getValidateMessages(emptyUsername)
    assertTrue(emptyViolations.contains(REGISTER_USERNAME_INVALID))

    val invalidUsername = RegistrationPayload("!@#user", emailAddress, password)
    val invalidViolation = validator.validate(invalidUsername).toList()
    assertEquals(REGISTER_USERNAME_INVALID, invalidViolation[0].message)

    val shortUsername = RegistrationPayload("sht", emailAddress, password)
    val shortViolations = validator.validate(shortUsername).toList()
    assertEquals(REGISTER_USERNAME_INVALID, shortViolations[0].message)

    val longString = RandomString.make(21)
    val longUsername = RegistrationPayload(longString, emailAddress, password)
    val longViolations = validator.validate(longUsername).toList()
    assertEquals(REGISTER_USERNAME_INVALID, longViolations[0].message)

  }

  @Test
  fun `emailAddress 는 필수입력, 최소 6 글자 이상, 최대 100자 이하, 계정@도메인 emailFormat`() {
    val emptyEmailAddress = RegistrationPayload(username, "   ", password)
    val emptyViolations = getValidateMessages(emptyEmailAddress)
    assertEquals(1, emptyViolations.size)
    assertTrue(emptyViolations.contains(REGISTER_EMAIL_ADDRESS_INVALID))

    val shortEmailAddress = RegistrationPayload(username, "a@b.c", password)
    val shortViolations = validator.validate(shortEmailAddress).toList()
    assertEquals(REGISTER_EMAIL_ADDRESS_INVALID, shortViolations[0].message)

    val longAccount = RandomString.make(50)
    val longDomain = RandomString.make(46) + ".com"
    val longEmailAddress = RegistrationPayload(username, "${longAccount}@${longDomain}", password)
    val longEmailViolations = validator.validate(longEmailAddress).toList()
    assertEquals(REGISTER_EMAIL_ADDRESS_INVALID, longEmailViolations[0].message)

    val invalidEmailAddress = RegistrationPayload(username, "invalid-email", password)
    val invalidViolations = validator.validate(invalidEmailAddress).toList()
    assertEquals(REGISTER_EMAIL_ADDRESS_INVALID, invalidViolations[0].message)
  }

  @Test
  fun `password 는 필수입력, 최소 6자 이상, 최대 128자 이하, 영문자 1개 이상과 숫자 1개 이상이 포함`() {
    val emptyPassword = RegistrationPayload(username, emailAddress, "   ")
    val emptyViolations = validator.validate(emptyPassword).toList()
    assertEquals(1, emptyViolations.size)
    assertEquals(REGISTER_PASSWORD_INVALID, emptyViolations[0].message)

    val shortPassword = RegistrationPayload(username, emailAddress, "ab123")
    val shortViolations = validator.validate(shortPassword).toList()
    assertEquals(REGISTER_PASSWORD_INVALID, shortViolations[0].message)

    val longStrPassword = "123" + RandomString.make(126)
    val longPassword = RegistrationPayload(username, emailAddress, longStrPassword)
    val longViolations = validator.validate(longPassword).toList()
    assertEquals(REGISTER_PASSWORD_INVALID, longViolations[0].message)

    val invalidPassword = RegistrationPayload(username, emailAddress, "12345678")
    val invalidViolations = validator.validate(invalidPassword).toList()
    assertEquals(REGISTER_PASSWORD_INVALID, invalidViolations[0].message)
  }

  @Test
  fun `Valid Payload`() {
    val payload = RegistrationPayload(username, emailAddress, password)
    val violations = validator.validate(payload)
    assertEquals(0, violations.size)
  }

  private fun getValidateMessages(payload: RegistrationPayload): List<String> {
    return validator.validate(payload).stream().map {
      it.message
    }.toList()
  }
}
