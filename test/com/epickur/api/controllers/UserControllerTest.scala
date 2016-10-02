package com.epickur.api.controllers

import com.epickur.api.entity.User
import com.epickur.api.utils.TestUtils
import org.scalatestplus.play._
import play.api.libs.json.Json
import play.api.test.FakeRequest
import play.api.test.Helpers._

class UserControllerTest extends PlaySpec with OneAppPerTest {

	"UserController" should {
		"Get user" in {
			val home = route(app, FakeRequest(GET, s"/users/${TestUtils.id}")).get
			status(home) mustBe OK
			contentType(home) mustBe Some(JSON)
			val user = contentAsJson(home).as[User]
			TestUtils.verifyUser(user)
		}
	}

	"UserController" should {
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

	"UserController" should {
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
	}
}
