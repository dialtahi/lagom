package com.lightbend.lagom.scaladsl.processmanager

import com.lightbend.lagom.scaladsl.persistence.PersistentEntityRegistry
import com.lightbend.lagom.scaladsl.server.LocalServiceLocator
import com.lightbend.lagom.scaladsl.testkit.ServiceTest
import org.scalatest.{Matchers, WordSpec}

class ProcessManagerSpec extends WordSpec with Matchers {

  lazy val server = ServiceTest.startServer(ServiceTest.defaultSetup) { ctx =>
    new ProcessManagerApplication(ctx) with LocalServiceLocator
  }

  "A Process Manager" should {

    "connect itself to event sources" in {
      val testProcessManager = server.application.processManagersRegistry
        // Persist an event
    }

  }

}
