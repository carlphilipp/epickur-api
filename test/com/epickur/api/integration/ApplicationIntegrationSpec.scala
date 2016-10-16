package com.epickur.api.integration

import play.api.libs.ws.WS
import play.api.test._


class ApplicationIntegrationSpec extends PlaySpecification {

	val url = "http://localhost:"

	"Application" should {
		"return 404 when accessing unknown resource" in new WithServer {
			val response = await(WS.url(url + port + "/boomz").get)
			response.status must equalTo(NOT_FOUND)
		}
	}
}
