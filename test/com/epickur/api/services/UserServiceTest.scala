package com.epickur.api.services

import com.epickur.api.dao.UserDAO
import com.epickur.api.entities.User
import com.epickur.api.utils.TestUtils
import org.mockito.Mockito._
import org.scalatest.mock.MockitoSugar
import org.scalatestplus.play.{OneAppPerTest, PlaySpec}
import play.api.libs.json.JsObject

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class UserServiceTest extends PlaySpec with OneAppPerTest with MockitoSugar {

	val userDAO = mock[UserDAO]
	when(userDAO.create(TestUtils.user)) thenReturn Future {}
	when(userDAO.read(TestUtils.id.toString)) thenReturn Future(List(User.getJsonUpdatedUser(null)))
	when(userDAO.update(TestUtils.user)) thenReturn Future {}

	val userService = new UserService(userDAO)

	"UserService" should {
		"Create user" in {
			val actual = userService.create(TestUtils.user)
			assert(actual.isInstanceOf[Future[Unit]])
			verify(userDAO).create(TestUtils.user)
		}
	}

	"UserService" should {
		"Get user" in {
			val actual = userService.read(TestUtils.id.toString)
			assert(actual.isInstanceOf[Future[List[JsObject]]])
			verify(userDAO).read(TestUtils.id.toString)
		}
	}

	"UserService" should {
		"Update user" in {
			val actual = userService.update(TestUtils.user)
			assert(actual.isInstanceOf[Future[Unit]])
			verify(userDAO).update(TestUtils.user)
		}
	}
}
