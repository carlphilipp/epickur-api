package com.epickur.api.dao

import com.epickur.api.utils.TestUtils
import org.scalatest.mock.MockitoSugar
import org.scalatestplus.play.{OneAppPerTest, PlaySpec}
import play.modules.reactivemongo.ReactiveMongoApi
import reactivemongo.api.MongoConnection

import scala.concurrent.ExecutionContext

class UserDAOTest extends PlaySpec with OneAppPerTest with MockitoSugar {

	// TODO use https://github.com/themillhousegroup/play2-reactivemongo-mocks
	// to mock all that

	val reactiveMongoApi = mock[ReactiveMongoApi]
	val exec = mock[ExecutionContext]
	val connection = mock[MongoConnection]
	val userDAO = new UserDAO(reactiveMongoApi)(exec)

	"UserDAO" should {
		"Create user" in {
			val user = userDAO.create(TestUtils.user)
			//TestUtils.verifyUser(user)
		}
	}

	"UserDAO" should {
		"Get user" in {
			val user = userDAO.read(TestUtils.id.toLong)
			//TestUtils.verifyUser(user)
		}
	}

	"UserDAO" should {
		"Update user" in {
			val user = userDAO.update(TestUtils.user)
			TestUtils.verifyUser(user)
		}
	}
}
