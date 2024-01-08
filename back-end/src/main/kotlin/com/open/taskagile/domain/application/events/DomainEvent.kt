package com.open.taskagile.domain.application.events

import java.io.Serializable
import java.util.Date

abstract class DomainEvent(val userId:Long, val createdDate: Date = Date()) : Serializable
