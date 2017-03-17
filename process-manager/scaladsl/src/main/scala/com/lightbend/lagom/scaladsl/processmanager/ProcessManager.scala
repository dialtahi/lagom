package com.lightbend.lagom.scaladsl.processmanager

/**
  * A `ProcessManager` is responsible of coordinate a business process
  */
abstract class ProcessManager {

  type State
  type DomainEvent

  type Transition = PartialFunction[DomainEvent, State]
  type Action = PartialFunction[State, Transition]

  class Actions extends (DomainEvent => Actions) {
    override def apply(event: DomainEvent): Actions = this


  }

  def correlationIdResolver[CorrelationId]: PartialFunction[DomainEvent, CorrelationId]

  def startWhen(initialization: Transition)(andThen: Action) = ???


}
