package com.lightbend.lagom.scaladsl.processmanager

trait MessageBrokerProcessManagerComponents extends ProcessManagerComponents {
  override lazy val processManagersRegistry: ProcessManagerRegistry =
    new CassandraPersistenceFMSProcessManagerRegistry(actorSystem)

}
