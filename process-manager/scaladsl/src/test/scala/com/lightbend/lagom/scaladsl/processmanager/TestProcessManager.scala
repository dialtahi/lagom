package com.lightbend.lagom.scaladsl.processmanager


class TestProcessManager extends ProcessManager {

  override type State = String
  override type DomainEvent = String

  override def correlationIdResolver[CorrelationId]: String = {
    case _ => ""
  }
}
