package com.lightbend.lagom.scaladsl.processmanager

import com.lightbend.lagom.scaladsl.broker.kafka.LagomKafkaClientComponents
import com.lightbend.lagom.scaladsl.persistence.cassandra.CassandraPersistenceComponents
import com.lightbend.lagom.scaladsl.playjson.{JsonSerializer, JsonSerializerRegistry}
import com.lightbend.lagom.scaladsl.server.{LagomApplication, LagomApplicationContext, LagomServer}
import com.softwaremill.macwire._
import play.api.libs.ws.ahc.AhcWSComponents

abstract class ProcessManagerApplication(context: LagomApplicationContext) extends LagomApplication(context)
  with CassandraPersistenceComponents
  with KafkaProcessManagerComponents
  with AhcWSComponents {

  override lazy val lagomServer: LagomServer = LagomServer.forServices(
    bindService[ProcessManagerService].to(wire[ProcessManagerServiceImp])
  )

  override lazy val jsonSerializerRegistry = ProcessManagerSerializerRegistry

  persistentEntityRegistry.register(wire[MyEntity])

  processManagersRegistry.register(wire[TestProcessManager])

}

object ProcessManagerSerializerRegistry extends JsonSerializerRegistry {
  override def serializers = List(
    JsonSerializer[InitializationEvent],
    JsonSerializer[SecondEvent],
    JsonSerializer[FirstCommand]
  )
}
