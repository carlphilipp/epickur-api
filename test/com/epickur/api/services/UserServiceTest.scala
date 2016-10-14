package com.epickur.api.services

import com.epickur.api.dao.UserDAO
import com.epickur.api.entities.User
import com.epickur.api.utils.TestUtils
import org.mockito.Mockito._
import org.scalatest.mock.MockitoSugar
import org.scalatest.{FeatureSpec, GivenWhenThen, Matchers}
import play.api.libs.json.JsObject

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class UserServiceTest extends FeatureSpec with GivenWhenThen with Matchers with MockitoSugar {

	val userDAO = org.scalatest.mock.MockitoSugar.mock[UserDAO]
	org.mockito.Mockito.when(userDAO.create(TestUtils.user)) thenReturn Future {}
	org.mockito.Mockito.when(userDAO.read(TestUtils.id.toString)) thenReturn Future(List(User.getJsonUpdatedUser(null)))
	org.mockito.Mockito.when(userDAO.update(TestUtils.user)) thenReturn Future {}
	val userService = new UserService(userDAO)

	info("I want to be able to verify that the user service layer do its job")

	feature("User service") {
		scenario("Create a user") {
			Given("a new user")
			val user = TestUtils.user

			When("passing it to the create service")
			val actual = userService.create(user)

			Then("the service returns a Future[unit]")
			assert(actual.isInstanceOf[Future[Unit]])

			Then("the dao layer has been called for user creation")
			verify(userDAO).create(TestUtils.user)
		}

		scenario("Get a user") {
			Given("a user id")
			val id = TestUtils.id.toString

			When("passing it to the read service")
			val actual = userService.read(id)

			Then("the service returns a Future[List[JsObject]]]")
			assert(actual.isInstanceOf[Future[List[JsObject]]])

			Then("the dao layer has been called for user read")
			verify(userDAO).read(TestUtils.id.toString)
		}

		scenario("Update a user") {
			Given("a user")
			val user = TestUtils.user

			When("passing it to the update service")
			val actual = userService.update(user)

			Then("the service returns a Future[Unit]")
			assert(actual.isInstanceOf[Future[Unit]])

			Then("the dao layer has been called for user update")
			verify(userDAO).update(TestUtils.user)
		}
	}
}
