package com.open.taskagile.application.domain.user.events

import com.open.taskagile.application.events.DomainEvent

class UserRegisteredEvent(source:Any, userId:Long) : DomainEvent(source, userId)
