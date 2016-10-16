package com.epickur.api.integration

import com.epickur.api.entities.User
import com.epickur.api.utils.UserUtils
import com.epickur.api.utils.UserUtils.verifyUser
import play.api.Application
import play.api.libs.json.{Json, Reads, Writes}
import play.api.libs.ws.WS
import play.api.test.Helpers._
import play.api.test._
import reactivemongo.bson.BSONObjectID


class UserIntegrationSpec extends PlaySpecification {

	implicit val jsonToUser: Reads[User] = User.jsonToUserWeb
	implicit val userToJson: Writes[User] = User.userToJsonWeb

	val url = "http://localhost:"
	val usersPath = "/users"

	"Application" should {

		"return 404 when accessing a non existing user" in new WithServer {
			val response = await(WS.url(url + port + usersPath + "/" + BSONObjectID.generate.stringify).get)
			response.status must equalTo(NOT_FOUND)
		}

		"create a user" in new WithServer {
			createAndValidateUser(port)
		}

		"get a user" in new WithServer {
			// Create user
			val user = createAndValidateUser(port)

			// Get that user
			val response = await(WS.url(url + port + usersPath + "/" + user.id.get).get)

			// Verify
			response.status must equalTo(OK)
			response.json must not be null
			val userResponse = response.json.as[User]
			verifyUser(userResponse)
		}

		"update a user" in new WithServer {
			// Create user
			val user = createAndValidateUser(port)
			val userUpdated = UserUtils.user
			userUpdated.id = user.id
			userUpdated.email = "newemail@email.com"

			// Update user
			val response = await(WS.url(url + port + usersPath + "/" + user.id.get).withHeaders(CONTENT_TYPE -> JSON).put(Json.toJson(userUpdated)))

			// Verify
			response.status must equalTo(OK)
			response.json must not be null
			val userResponse = response.json.as[User]
			assert(userResponse.email == "newemail@email.com")
		}
	}

	private def createAndValidateUser(port: Int)(implicit application: Application) = {
		val user = UserUtils.userAsString

		val response = await(WS.url(url + port + usersPath).withHeaders(CONTENT_TYPE -> JSON).post(Json.parse(user)))

		response.status must equalTo(OK)
		response.json must not be null
		val userResponse = response.json.as[User]
		verifyUser(userResponse)
		userResponse
	}
}
