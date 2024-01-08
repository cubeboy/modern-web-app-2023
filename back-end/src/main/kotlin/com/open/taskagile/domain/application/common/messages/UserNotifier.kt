package com.open.taskagile.domain.application.common.messages

interface UserNotifier {
  fun notify(userId: Long, notificationType: NotificationType)
}
