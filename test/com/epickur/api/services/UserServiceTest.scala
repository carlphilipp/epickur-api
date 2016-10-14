package com.epickur.api.services

import com.epickur.api.dao.UserDAO
import com.epickur.api.entities.{Role, User}
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
			val originalPassword = user.password

			When("passing it to the create service")
			val actual = userService.create(user)

			Then("the service returns a Future[unit]")
			assert(actual.isInstanceOf[Future[Unit]])

			Then("a random user id is generated")
			assert(user.id != null)

			Then("the user role is 'user'")
			assert(user.role.isDefined)
			assert(user.role.get == Role.user)

			Then("the user creation date is not null")
			assert(user.createdAt.isDefined)
			assert(user.createdAt.get != null)

			Then("the user update date is not null")
			assert(user.updatedAt.isDefined)
			assert(user.updatedAt.get != null)

			Then("the user password is encoded")
			assert(user.password != originalPassword)

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

			Then("the user password is null")
			assert(user.password == null)

			Then("the user roll is empty")
			assert(user.role.isEmpty)

			Then("the user allow is empty")
			assert(user.allow.isEmpty)

			Then("the user key is empty")
			assert(user.key.isEmpty)

			Then("the user newPassword is empty")
			assert(user.newPassword.isEmpty)

			Then("the user creation date must not be modified")
			assert(user.createdAt.isEmpty)

			Then("the user update date is not null")
			assert(user.updatedAt.isDefined)
			assert(user.updatedAt.get != null)

			Then("the dao layer has been called for user update")
			verify(userDAO).update(TestUtils.user)
		}
	}
}
