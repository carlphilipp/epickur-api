package com.epickur.api.controllers

import org.scalatest.MustMatchers._
import org.scalatest.{FeatureSpec, GivenWhenThen, Matchers}
import org.scalatestplus.play._
import play.api.test.FakeRequest
import play.api.test.Helpers._

/**
  * Add your spec here.
  * You can mock out a whole application including requests, plugins etc.
  * For more information, consult the wiki.
  */
class RouteSpec extends FeatureSpec with OneAppPerTest with GivenWhenThen with Matchers {

	feature("Routes") {
		scenario("Random resource access") {
			Given("a resource that does not exit")
			val resource = "/boomz"

			When("trying to access the resource")
			val request = route(app, FakeRequest(GET, resource)).get

			Then("the status should be 404")
			status(request) mustBe NOT_FOUND
		}
	}
}
