package com.lightbend.lagom.scaladsl.processmanager

import com.lightbend.lagom.scaladsl.persistence.{PersistentEntity, PersistentEntityRegistry}

import scala.concurrent.ExecutionContext

sealed trait ProcessManagerEvent
case class InitializationEvent(correlationId: String) extends ProcessManagerEvent
case class SecondEvent(correlationId: String) extends ProcessManagerEvent
case class ThirdEvent(correlationId: String) extends ProcessManagerEvent

sealed trait ProcessManagerState
object Initialized extends ProcessManagerState
case class ProcessingSecondEvent() extends ProcessManagerState
case class ProcessingThirdEvent() extends ProcessManagerState

sealed trait Command
case class FirstCommand() extends Command

class MyEntity extends PersistentEntity {
  override type Command = this.type
  override type Event = this.type
  override type State = this.type

  override def initialState: MyEntity.this.type = ???

  /**
    * Abstract method that must be implemented by concrete subclass to define
    * the behavior of the entity.
    */
  override def behavior: Behavior = ???
}

class TestProcessManager(persistentEntityRegistry: PersistentEntityRegistry)(implicit ec: ExecutionContext)
  extends ProcessManager {

  override type State = ProcessManagerState
  override type DomainEvent = ProcessManagerEvent

  override def correlationIdResolver[String]: String = {
    case InitializationEvent(correlationId) => correlationId
    case SecondEvent(correlationId) => correlationId
    case ThirdEvent(correlationId) => correlationId
  }

  startWhen {
    case InitializationEvent(_) => Initialized
  } andThen {
      case Initialized => {
        case SecondEvent(_) =>
          persistentEntityRegistry.refFor[MyEntity](correlationIdResolver).ask(FirstCommand)
          ProcessingSecondEvent()
      }
      case ProcessingSecondEvent() => {
        case ThirdEvent(_) => ProcessingThirdEvent()
      }
  }

}
