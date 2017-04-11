package com.lightbend.lagom.scaladsl.processmanager

trait KafkaProcessManagerComponents extends ProcessManagerComponents with LagomKafkaClientComponents {
  override lazy val processManagersRegistry: ProcessManagerRegistry =
    new CassandraPersistenceFMSProcessManagerRegistry(actorSystem)

}
