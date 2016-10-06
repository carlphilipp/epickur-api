package com.epickur.api.services

import com.epickur.api.dao.UserDAO
import com.epickur.api.utils.TestUtils
import org.mockito.Mockito._
import org.scalatest.mock.MockitoSugar
import org.scalatestplus.play.{OneAppPerTest, PlaySpec}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class UserServiceTest extends PlaySpec with OneAppPerTest with MockitoSugar {

	val userDAO = mock[UserDAO]
	when(userDAO.create(TestUtils.user)) thenReturn Future {}
	//when(userDAO.read(TestUtils.id.toLong)) thenReturn TestUtils.user
	when(userDAO.update(TestUtils.user)) thenReturn TestUtils.user
	val userService = new UserService(userDAO)

	"UserService" should {
		"Create user" in {
			// TODO check how to verify a future
			val user = userService.create(TestUtils.user)
			//TestUtils.verifyUser(user)
		}
	}

/*	"UserService" should {
		"Get user" in {
			val user = userService.read(TestUtils.id.toLong)
			//TestUtils.verifyUser(user)
		}
	}*/

	"UserService" should {
		"Update user" in {
			val user = userService.update(TestUtils.user)
			TestUtils.verifyUser(user)
		}
	}
}
