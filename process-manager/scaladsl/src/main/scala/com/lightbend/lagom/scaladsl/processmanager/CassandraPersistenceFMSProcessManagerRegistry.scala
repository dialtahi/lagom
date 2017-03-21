package com.lightbend.lagom.scaladsl.processmanager

import javax.inject.Inject

import akka.actor.ActorSystem

private[lagom] final class CassandraPersistenceFMSProcessManagerRegistry @Inject() (actorSystem: ActorSystem)
  extends AbstractProcessManagerRegistry(actorSystem) {

}
