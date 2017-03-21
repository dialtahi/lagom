package com.lightbend.lagom.scaladsl.processmanager

import java.util.concurrent.ConcurrentHashMap

import akka.actor.ActorSystem
import akka.cluster.Cluster
import akka.cluster.sharding.{ClusterSharding, ClusterShardingSettings, ShardRegion}
import com.lightbend.lagom.internal.persistence.cluster.ClusterDistribution.EnsureActive

abstract class AbstractProcessManagerRegistry(system: ActorSystem) extends ProcessManagerRegistry {

  private val sharding = ClusterSharding(system)

  private val conf = system.settings.config.getConfig("lagom.process-manager")

  private val role: Option[String] = conf.getString("run-entities-on-role") match {
    case "" => None
    case r  => Some(r)
  }

  private val shardingSettings = ClusterShardingSettings(system).withRole(role)

  private val extractEntityId: ShardRegion.ExtractEntityId = {
    case msg @ EnsureActive(entityId) => (entityId, msg)
  }
  private val extractShardId: ShardRegion.ExtractShardId = {
    case EnsureActive(entityId) => entityId
  }

  private val registeredTypeNames = new ConcurrentHashMap[String, Class[_]]()
  private val reverseRegister = new ConcurrentHashMap[Class[_], String]()

  override def register(processFactory: => ProcessManager): Unit = {

    // try to create one instance to fail fast
    val proto = processFactory
    val processManagerTypeName = proto.processManagerTypeName
    val processManagerClass = proto.getClass

    // detect non-unique short class names, since that is used as sharding type name
    val alreadyRegistered = registeredTypeNames.putIfAbsent(processManagerTypeName, processManagerClass)
    if (alreadyRegistered != null && !alreadyRegistered.equals(processManagerClass)) {
      throw new IllegalArgumentException(s"The processManagerTypeName [$processManagerTypeName] for process manager " +
        s"[${processManagerClass.getName}] is not unique. It is already registered by [${alreadyRegistered.getName}]. " +
        "Override processManagerTypeName in the ProcessManager to define a unique name.")
    }
    // if the processManagerTypeName is deemed unique, we add the process manager to the reverse index:
    reverseRegister.putIfAbsent(processManagerClass, processManagerTypeName)

    if (role.forall(Cluster(system).selfRoles.contains)) {
      val entityProps = ProcessManagerPersistentFSM.props(
        persistenceIdPrefix = processManagerTypeName, None, () => processFactory
      )
      sharding.start(processManagerTypeName, entityProps, shardingSettings, extractEntityId, extractShardId)
    } else {
      // not required role, start in proxy mode
      sharding.startProxy(processManagerTypeName, role, extractEntityId, extractShardId)
    }
  }
}
