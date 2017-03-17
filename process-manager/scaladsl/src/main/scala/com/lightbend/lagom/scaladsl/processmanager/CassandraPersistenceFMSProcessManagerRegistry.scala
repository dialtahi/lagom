package com.lightbend.lagom.scaladsl.processmanager

import akka.actor.ActorSystem

private[lagom] final class CassandraPersistenceFMSProcessManagerRegistry(actorSystem: ActorSystem) extends ProcessManagerRegistry {
  /**
    * At system startup all [[com.lightbend.lagom.scaladsl.processmanager.ProcessManager]]
    * classes must be registered with this method.
    *
    * The `processFactory` will be called when a new initialization event instance is to be created.
    * That will happen in another thread, so the `processFactory` must be thread-safe, e.g.
    * not close over shared mutable state that is not thread-safe.
    */
  override def register(processFactory: => ProcessManager): Unit = ???
}
