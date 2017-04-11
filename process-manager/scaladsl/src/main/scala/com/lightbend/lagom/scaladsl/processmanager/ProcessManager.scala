package com.lightbend.lagom.scaladsl.processmanager

import akka.event.Logging
import com.lightbend.lagom.internal.scaladsl.broker.kafka.ScaladslKafkaSubscriber
import com.lightbend.lagom.scaladsl.api.broker.Subscriber

/**
  * A `ProcessManager` is responsible of coordinate a business process
  */
abstract class ProcessManager {

  type State
  type DomainEvent

  type Transition = PartialFunction[DomainEvent, State]
  type Action = PartialFunction[State, Transition]

  /**
    * The name of this process manager type. It should be unique among the process manager
    * types of the service. By default it is using the short class name
    * of the concrete `ProcessManager` class. Subclass may override
    * to define other type names. It is needed to override and retain
    * the original name when the class name is changed because this name
    * is part of the key of the store data (it is part of the `persistenceId`
    * of the underlying `PersistentFSM`).
    */
  def processManagerTypeName: String = Logging.simpleName(getClass)

  class Actions extends (DomainEvent => Actions) {
    override def apply(event: DomainEvent): Actions = this


  }

  def a() = {

  }

  def correlationIdResolver[CorrelationId]: PartialFunction[DomainEvent, CorrelationId]

  def startWhen(initialization: Transition)(andThen: Action) = ???


}
