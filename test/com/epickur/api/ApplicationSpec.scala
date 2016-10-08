package com.epickur.api

import com.epickur.api.entities.User
import com.epickur.api.utils.TestUtils
import org.scalatest.MustMatchers._
import org.scalatest.{FeatureSpec, GivenWhenThen, Matchers}
import org.scalatestplus.play._
import play.api.libs.json.{Json, OWrites, Reads}
import play.api.test.FakeRequest
import play.api.test.Helpers._
import reactivemongo.bson.BSONObjectID

/**
  * Add your spec here.
  * You can mock out a whole application including requests, plugins etc.
  * For more information, consult the wiki.
  */
class ApplicationSpec extends FeatureSpec with OneAppPerTest with GivenWhenThen with Matchers {

	implicit val userToJson: OWrites[User] = User.userToJsonWeb
	implicit val jsonToUser: Reads[User] = User.jsonToUserWeb

	info("As a consumer of the API")
	info("I want to be able to verify that basic features are working")

	feature("Routes") {
		scenario("Random resource access") {
			Given("a resource that does not exit")
			val resource = "/boomz"

			When("trying to access the resource")
			val request = route(app, FakeRequest(GET, resource)).get

			Then("the status should be 404")
			status(request) mustBe NOT_FOUND
		}

		scenario("Get user that does not exist") {
			Given("a random user id")
			val currentUserId = BSONObjectID.generate.stringify

			When("doing a request to access the user matching that id")
			val request = route(app, FakeRequest(GET, s"/users/$currentUserId")).get

			Then("the status should be 404")
			status(request) mustBe NOT_FOUND

			Then("the content type should not be set")
			contentType(request) mustBe None
		}

		scenario("Create a new user") {
			Given("a new user")
			val user = TestUtils.userAsString

			When("doing a request to create it")
			val fakeRequest = FakeRequest(POST, "/users")
				.withJsonBody(Json.parse(user))
				.withHeaders(CONTENT_TYPE -> JSON)
			val request = route(app, fakeRequest).get

			Then("the content type should be 303")
			status(request) mustBe SEE_OTHER
		}

		scenario("Get a user") {
			// Setup some dummy data into DB before being able to test it
		}

		scenario("Update a user") {
			// Setup some dummy data into DB before being able to test it
		}
	}
}
