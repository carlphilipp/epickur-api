import com.epickur.entity.User
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

	"Routes" should {
		"Send 404 on a bad request" in {
			route(app, FakeRequest(GET, "/boomz")).map(status) mustBe Some(NOT_FOUND)
		}
	}

	"UserController" should {
		"Get current user" in {
			val home = route(app, FakeRequest(GET, s"/users/${TestUtils.id}")).get
			status(home) mustBe OK
			contentType(home) mustBe Some("application/json")
			val user = contentAsJson(home).as[User]
			verifyUser(user)
		}
	}

	"UserController" should {
		"Create user" in {
			val fakeRequest = FakeRequest(POST, "/users")
				.withJsonBody(Json.parse(TestUtils.user))
				.withHeaders(CONTENT_TYPE -> "application/json")

			val home = route(app, fakeRequest).get
			status(home) mustBe OK
			contentType(home) mustBe Some("application/json")
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
