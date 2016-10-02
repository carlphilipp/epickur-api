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
}
