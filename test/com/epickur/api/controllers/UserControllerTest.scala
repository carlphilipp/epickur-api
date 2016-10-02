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
			verifyUser(user)
		}
	}

	"UserController" should {
		"Create user" in {
			val fakeRequest = FakeRequest(POST, "/users")
				.withJsonBody(Json.parse(TestUtils.user))
				.withHeaders(CONTENT_TYPE -> JSON)

			val home = route(app, fakeRequest).get
			status(home) mustBe OK
			contentType(home) mustBe Some(JSON)
			val user = contentAsJson(home).as[User]
			verifyUser(user)
		}
	}

	"UserController" should {
		"Update user" in {
			val fakeRequest = FakeRequest(PUT, s"/users/${TestUtils.id}")
				.withJsonBody(Json.parse(TestUtils.user))
				.withHeaders(CONTENT_TYPE -> JSON)

			val home = route(app, fakeRequest).get
			status(home) mustBe OK
			contentType(home) mustBe Some(JSON)
			val user = contentAsJson(home).as[User]
			verifyUser(user)
		}
	}

	def verifyUser(user: User) = {
		assert(user.id.getOrElse(fail) == TestUtils.id.toLong)
		assert(user.name == TestUtils.name)
		assert(user.first == TestUtils.first)
		assert(user.last == TestUtils.last)
		assert(user.password == TestUtils.password)
		assert(user.email == TestUtils.email)
		assert(user.zipcode == TestUtils.zipcode)
		assert(user.state == TestUtils.state)
		assert(user.country == TestUtils.country)
		assert(user.allow == TestUtils.allow.toInt)
		assert(user.createdAt != null)
		assert(user.updatedAt != null)
	}
}
