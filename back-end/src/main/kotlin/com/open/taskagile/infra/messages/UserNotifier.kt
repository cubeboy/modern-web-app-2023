package com.open.taskagile.infra.messages

interface UserNotifier {
  fun notify(userId: Long, notificationType: UserNotificationType)
}
