package com.open.taskagile.application.domain.common.security

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

@Component
class PasswordEncryptorDelegator(val passwordEncoder:PasswordEncoder): PasswordEncryptor {
  //TODO 암호화 구현체 필요
  override fun encrypt(rawPassword: String): Mono<String> {
    return passwordEncoder.encode(rawPassword).toMono()
  }
}
