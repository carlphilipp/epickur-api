package com.epickur.api

import com.epickur.api.entities.User
import com.epickur.api.utils.TestUtils
import org.scalatestplus.play._
import play.api.libs.json.Json
import play.api.test.FakeRequest
import play.api.test.Helpers._

/**
  * Add your spec here.
  * You can mock out a whole application including requests, plugins etc.
  * For more information, consult the wiki.
  */
class ApplicationSpec extends PlaySpec with OneAppPerTest {

	"Routes" should {
		"Send 404 on a bad request" in {
			route(app, FakeRequest(GET, "/boomz")).map(status) mustBe Some(NOT_FOUND)
		}
	}

	/*"User" should {
		"Get user" in {
			val home = route(app, FakeRequest(GET, s"/users/${TestUtils.id}")).get
			status(home) mustBe OK
			contentType(home) mustBe Some(JSON)
			val user = contentAsJson(home).as[User]
			TestUtils.verifyUser(user)
		}
	}

	"User" should {
		"Create user" in {
			val fakeRequest = FakeRequest(POST, "/users")
				.withJsonBody(Json.parse(TestUtils.userAsString))
				.withHeaders(CONTENT_TYPE -> JSON)

			val home = route(app, fakeRequest).get
			status(home) mustBe OK
			contentType(home) mustBe Some(JSON)
			val user = contentAsJson(home).as[User]
			TestUtils.verifyUser(user)
		}
	}

	"User" should {
		"Update user" in {
			val fakeRequest = FakeRequest(PUT, s"/users/${TestUtils.id}")
				.withJsonBody(Json.parse(TestUtils.userAsString))
				.withHeaders(CONTENT_TYPE -> JSON)

			val home = route(app, fakeRequest).get
			status(home) mustBe OK
			contentType(home) mustBe Some(JSON)
			val user = contentAsJson(home).as[User]
			TestUtils.verifyUser(user)
		}
	}*/
}
