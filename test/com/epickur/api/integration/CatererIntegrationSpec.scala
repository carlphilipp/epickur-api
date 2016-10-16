package com.epickur.api.integration

import com.epickur.api.entities.Caterer
import com.epickur.api.utils.CatererUtils
import com.epickur.api.utils.CatererUtils.verifyCaterer
import play.api.Application
import play.api.libs.json.{Json, Reads, Writes}
import play.api.libs.ws.WS
import play.api.test.Helpers._
import play.api.test._
import reactivemongo.bson.BSONObjectID


class CatererIntegrationSpec extends PlaySpecification {

	implicit val jsonToCaterer: Reads[Caterer] = Caterer.jsonToCatererWeb
	implicit val catererToJson: Writes[Caterer] = Caterer.catererToJsonWeb

	val url = "http://localhost:"
	val caterersPath = "/caterers"

	"Application" should {

		"return 404 when accessing a non existing caterer" in new WithServer {
			val response = await(WS.url(url + port + caterersPath + "/" + BSONObjectID.generate.stringify).get)
			response.status must equalTo(NOT_FOUND)
		}

		"create a caterer" in new WithServer {
			createAndValidateCaterer(port)
		}

		"get a caterer" in new WithServer {
			// Create caterer
			val caterer = createAndValidateCaterer(port)

			// Get that caterer
			val response = await(WS.url(url + port + caterersPath + "/" + caterer.id.get).get)

			// Verify
			response.status must equalTo(OK)
			response.json must not be null
			val catererResponse = response.json.as[Caterer]
			verifyCaterer(catererResponse)
		}

		"update a caterer" in new WithServer {
			// Create caterer
			val caterer = createAndValidateCaterer(port)
			val catererUpdated = CatererUtils.caterer
			catererUpdated.id = caterer.id
			catererUpdated.email = "newemail@email.com"

			// Update that caterer
			val response = await(WS.url(url + port + caterersPath + "/" + caterer.id.get).withHeaders(CONTENT_TYPE -> JSON).put(Json.toJson(catererUpdated)))

			// Verify
			response.status must equalTo(OK)
			response.json must not be null
			val catererResponse = response.json.as[Caterer]
			assert(catererResponse.email == "newemail@email.com")
		}
	}

	private def createAndValidateCaterer(port: Int)(implicit application: Application) = {
		val caterer = CatererUtils.catererAsString

		val response = await(WS.url(url + port + caterersPath).withHeaders(CONTENT_TYPE -> JSON).post(Json.parse(caterer)))

		response.status must equalTo(OK)
		response.json must not be null
		val catererResponse = response.json.as[Caterer]
		verifyCaterer(catererResponse)
		catererResponse
	}
}
