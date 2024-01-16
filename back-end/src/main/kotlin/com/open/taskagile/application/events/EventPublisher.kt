package com.open.taskagile.application.events

interface EventPublisher {
  fun publish(domainEvent: DomainEvent)
}
