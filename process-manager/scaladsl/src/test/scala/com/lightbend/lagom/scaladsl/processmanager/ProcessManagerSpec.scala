package com.lightbend.lagom.scaladsl.processmanager

import org.scalatest.{Matchers, WordSpec}

class ProcessManagerSpec extends WordSpec with Matchers {

  "A Process Manager" should {

    "connect itself to event sources" in {
      val testProcessManager = new TestProcessManager

    }

  }

}
