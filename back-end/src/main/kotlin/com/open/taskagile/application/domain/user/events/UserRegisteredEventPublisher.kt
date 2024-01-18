package com.open.taskagile.application.domain.user.events

import com.open.taskagile.application.events.DomainEvent
import com.open.taskagile.application.events.EventPublisher
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class UserRegisteredEventPublisher : EventPublisher {
  private val log = LoggerFactory.getLogger(UserRegisteredEventPublisher::class.java)
  override fun publish(domainEvent: DomainEvent) {
    log.debug("UserRegisteredEvent Raised")
  }
}
