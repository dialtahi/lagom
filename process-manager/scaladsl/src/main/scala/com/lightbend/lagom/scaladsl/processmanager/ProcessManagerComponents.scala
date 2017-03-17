package com.lightbend.lagom.scaladsl.processmanager

import com.lightbend.lagom.scaladsl.cluster.ClusterComponents

/**
  * Created by Administrator on 16/03/2017.
  */
trait ProcessManagerComponents extends ClusterComponents {
  def processManagersRegistry: ProcessManagerRegistry
}
