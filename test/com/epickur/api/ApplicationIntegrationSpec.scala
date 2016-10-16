package com.epickur.api

import com.epickur.api.entities.User
import com.epickur.api.utils.TestUtils
import com.epickur.api.utils.TestUtils.verifyUser
import play.api.Application
import play.api.libs.json.{Json, Reads, Writes}
import play.api.libs.ws.WS
import play.api.test.Helpers._
import play.api.test._
import reactivemongo.bson.BSONObjectID


class ApplicationIntegrationSpec extends PlaySpecification {

	implicit val jsonToUser: Reads[User] = User.jsonToUserWeb
	implicit val userToJson: Writes[User] = User.userToJsonWeb

	val url = "http://localhost:"

	"Application" should {
		"return 404 when accessing unknown resource" in new WithServer {
			val response = await(WS.url(url + port + "/boomz").get)
			response.status must equalTo(NOT_FOUND)
		}

		"return 404 when accessing a non existing user" in new WithServer {
			val response = await(WS.url(url + port + "/users/" + BSONObjectID.generate.stringify).get)
			response.status must equalTo(NOT_FOUND)
		}

		"create a user" in new WithServer {
			createAndValidateUser(port)
		}

		"get a user" in new WithServer {
			// Create user
			val user = createAndValidateUser(port)

			// Get that user
			val response = await(WS.url(url + port + "/users/" + user.id.get).get)

			response.status must equalTo(OK)
			response.json must not be null
			val userResponse = response.json.as[User]
			verifyUser(userResponse)
		}

		"update a user" in new WithServer {
			// Create user
			val user = createAndValidateUser(port)
			val userUpdated = TestUtils.user
			userUpdated.id = user.id
			userUpdated.email = "newemail@email.com"

			// Update user
			val response = await(WS.url(url + port + "/users/" + user.id.get).withHeaders(CONTENT_TYPE -> JSON).put(Json.toJson(userUpdated)))

			response.status must equalTo(OK)
			response.json must not be null
			val userResponse = response.json.as[User]
			assert(userResponse.email == "newemail@email.com")
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
