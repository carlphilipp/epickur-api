package com.epickur.api.dao

import com.epickur.api.utils.TestUtils
import org.scalatestplus.play.{OneAppPerTest, PlaySpec}

class UserDAOTest extends PlaySpec with OneAppPerTest {

	val userDAO = new UserDAO

	"UserDAO" should {
		"Create user" in {
			val user = userDAO.create(TestUtils.user)
			TestUtils.verifyUser(user)
		}
	}

	"UserDAO" should {
		"Get user" in {
			val user = userDAO.read(TestUtils.id.toLong)
			TestUtils.verifyUser(user)
		}
	}

	"UserDAO" should {
		"Update user" in {
			val user = userDAO.update(TestUtils.user)
			TestUtils.verifyUser(user)
		}
	}
}
