package com.open.taskagile.application.events

import java.io.Serializable
import java.util.*

abstract class DomainEvent(val id: Long, val cratedDate: Date = Date()): Serializable
