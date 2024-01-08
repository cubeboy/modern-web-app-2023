package com.open.taskagile.domain.application.events

interface EventPublisher {
  fun publish(domainEvent: DomainEvent)
}
