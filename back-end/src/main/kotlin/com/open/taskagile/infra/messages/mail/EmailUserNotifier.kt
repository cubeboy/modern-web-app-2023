package com.open.taskagile.infra.messages.mail

import com.open.taskagile.infra.messages.UserNotificationType
import com.open.taskagile.infra.messages.UserNotifier
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class EmailUserNotifier: UserNotifier {
  private val log = LoggerFactory.getLogger(EmailUserNotifier::class.java)
  override fun notify(userId: Long, notificationType: UserNotificationType) {
    log.debug("Email User Notifier userId: {}, UserNotificationType: {}", userId, notificationType)
  }
}
