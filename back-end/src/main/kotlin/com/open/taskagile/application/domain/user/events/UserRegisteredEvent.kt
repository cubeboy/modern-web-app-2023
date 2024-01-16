package com.open.taskagile.application.domain.user.events

import com.open.taskagile.application.events.DomainEvent

class UserRegisteredEvent(userId:Long) : DomainEvent(userId)
