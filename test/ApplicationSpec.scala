import org.scalatestplus.play._
import play.api.libs.json.Json
import play.api.test.Helpers._
import play.api.test._

/**
  * Add your spec here.
  * You can mock out a whole application including requests, plugins etc.
  * For more information, consult the wiki.
  */
class ApplicationSpec extends PlaySpec with OneAppPerTest {

	var user = """{"id":"123787891","name":"carlphilipp","first":"carl","last":"harmant","password":"mypassword","email":"cp.harmant@gmail.com","zipcode":"60614","state":"Illinois","country":"USA","allow":0,"createdAt":"2016-10-01T19:41:00.683","updatedAt":"2016-10-01T19:41:00.685"}"""

	"Routes" should {

		"Send 404 on a bad request" in {
			route(app, FakeRequest(GET, "/boum")).map(status) mustBe Some(NOT_FOUND)
		}
	}

	"UserController" should {

		"Get current user" in {
			val home = route(app, FakeRequest(GET, "/users")).get

			status(home) mustBe OK
			contentType(home) mustBe Some("application/json")
			contentAsString(home) must include("carl")
		}
	}

	"UserController" should {

		"Create user" in {
			val fakeRequest = FakeRequest(POST, "/users")
				.withJsonBody(Json.parse(user))
				.withHeaders(CONTENT_TYPE -> "application/json")

			val home = route(app, fakeRequest).get

			status(home) mustBe OK
			contentType(home) mustBe Some("application/json")
			contentAsString(home) must include("carl")
		}
	}
}
