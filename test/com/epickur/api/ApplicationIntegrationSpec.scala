package com.epickur.api

import com.epickur.api.entities.User
import com.epickur.api.utils.TestUtils
import com.epickur.api.utils.TestUtils.verifyUser
import play.api.Application
import play.api.libs.json.{Json, Reads}
import play.api.libs.ws.WS
import play.api.test.Helpers._
import play.api.test._


class ApplicationIntegrationSpec extends PlaySpecification {

	implicit val jsonToUser: Reads[User] = User.jsonToUserWeb

	val url = "http://localhost:"

	"Application" should {
		"return 404 when accessing unknown resource" in new WithServer {
			val response = await(WS.url(url + port + "/boomz").get)
			response.status must equalTo(NOT_FOUND)
		}

		"create a user" in new WithServer {
			createAndValidateUser(port)
		}

		"read a user" in new WithServer {
			// Create user
			val user = createAndValidateUser(port)

			// Update user
			val response = await(WS.url(url + port + "/users/" + user.id.get).get)

			response.status must equalTo(OK)
			response.json must not be null
			val userResponse = response.json.as[User]
			verifyUser(userResponse)
		}
	}

	private def createAndValidateUser(port: Int)(implicit application: Application) = {
		val user = TestUtils.userAsString

		val response = await(WS.url(url + port + "/users").withHeaders(CONTENT_TYPE -> JSON).post(Json.parse(user)))

		response.status must equalTo(OK)
		response.json must not be null
		val userResponse = response.json.as[User]
		verifyUser(userResponse)
		userResponse
	}
}
