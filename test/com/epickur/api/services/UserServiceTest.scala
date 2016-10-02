package com.epickur.api.services

import com.epickur.api.utils.TestUtils
import org.scalatestplus.play.{OneAppPerTest, PlaySpec}

class UserServiceTest extends PlaySpec with OneAppPerTest {

	val userService = new UserService()

	"UserService" should {
		"Create user" in {
			val user = userService.create(TestUtils.user)
			TestUtils.verifyUser(user)
		}
	}

	"UserService" should {
		"Get user" in {
			val user = userService.read(TestUtils.id.toLong)
			TestUtils.verifyUser(user)
		}
	}

	"UserService" should {
		"Update user" in {
			val user = userService.update(TestUtils.user)
			TestUtils.verifyUser(user)
		}
	}
}
