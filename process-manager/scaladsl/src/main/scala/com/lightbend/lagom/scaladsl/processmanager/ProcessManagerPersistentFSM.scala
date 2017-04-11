package com.lightbend.lagom.scaladsl.processmanager

import java.net.URLDecoder

import akka.actor.Props
import akka.persistence.fsm.PersistentFSM
import akka.util.ByteString
import play.api.Logger

private[lagom] object ProcessManagerPersistentFSM {
  def props(
    persistenceIdPrefix:       String,
    id:                        Option[String],
    processManagerFactory:     () => ProcessManager
  ): Props = Props(new ProcessManagerPersistentFSM(persistenceIdPrefix, id, processManagerFactory()))

  val EntityIdSeparator = '|'

  /**
    * @return the entity id part encoded in the persistence id
    */
  def extractEntityId(persistenceId: String): String = {
    val idx = persistenceId.indexOf(EntityIdSeparator)
    if (idx > 0) {
      persistenceId.substring(idx + 1)
    } else throw new IllegalArgumentException(
      s"Cannot split '$persistenceId' into persistenceIdPrefix and entityId " +
        s"because there is no separator character ('$EntityIdSeparator')"
    )
  }

}

sealed trait ProcessManagerData

private[lagom] class ProcessManagerPersistentFSM(
  persistenceIdPrefix:       String,
  id:                        Option[String],
  processManager:            ProcessManager
) extends PersistentFSM[ProcessManagerData, processManager.DomainEvent, processManager.State] {

  private val log = Logger(this.getClass)

  private val entityId: String = id.getOrElse(
    URLDecoder.decode(self.path.name, ByteString.UTF_8)
  )
  override val persistenceId: String = persistenceIdPrefix + entityId

  override implicit def domainEventClassTag: ClassManifest[processManager.State] = ???

  override def applyEvent(domainEvent: processManager.State, currentData: processManager.DomainEvent): processManager.DomainEvent =
    ???
}
