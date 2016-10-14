package com.epickur.api.integration

import org.scalatest.TestData
import org.scalatestplus.play.{HtmlUnitFactory, OneBrowserPerTest, PlaySpec, ServerProvider}
import play.api.Application
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.test.{Helpers, TestServer}

class ApplicationIntegrationSpec extends PlaySpec with OneBrowserPerTest with HtmlUnitFactory with ServerProvider {

	private var privateApp: Application = _

	/**
	  * Implicit method that returns the `FakeApplication` instance for the current test.
	  */
	implicit final def app: Application = synchronized {
		privateApp
	}

	/**
	  * Creates new instance of `Application` with parameters set to their defaults. Override this method if you
	  * need an `Application` created with non-default parameter values.
	  */
	def newAppForTest(testData: TestData): Application = GuiceApplicationBuilder().build()

	lazy val port: Int = Helpers.testServerPort

	override def withFixture(test: NoArgTest) = {
		synchronized {
			privateApp = newAppForTest(test)
		}
		Helpers.running(TestServer(port, app)) {
			super.withFixture(test)
		}
	}

	"Application" should {

		"work from within a browser" in {
			go to ("http://localhost:" + port + "/users/58002d3c5700005700e02f28")

			pageSource must include("carl")
		}
	}
}
