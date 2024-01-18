package com.open.taskagile.application.events

import org.springframework.context.ApplicationEvent

abstract class DomainEvent(source: Any, val userId: Long):
  ApplicationEvent(source)
