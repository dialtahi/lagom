package com.lightbend.lagom.scaladsl.processmanager

/**
  * A `ProcessManager` is responsible of coordinate a business process
  */
abstract class ProcessManager {

  type State
  type DomainEvent

  def correlationIdResolver[CorrelationId]: PartialFunction[DomainEvent, CorrelationId]

}
