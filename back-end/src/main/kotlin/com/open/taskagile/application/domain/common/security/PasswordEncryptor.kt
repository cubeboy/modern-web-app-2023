package com.open.taskagile.application.domain.common.security

import reactor.core.publisher.Mono

interface PasswordEncryptor {
  fun encrypt(rawPassword: String): Mono<String>
}
