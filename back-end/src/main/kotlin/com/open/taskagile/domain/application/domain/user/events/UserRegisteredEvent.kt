package com.open.taskagile.domain.application.domain.user.events

import com.open.taskagile.domain.application.events.DomainEvent

class UserRegisteredEvent(userId:Long) : DomainEvent(userId)
