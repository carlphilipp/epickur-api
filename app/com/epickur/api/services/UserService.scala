package com.epickur.api.services

import java.time.LocalDateTime
import javax.inject.{Inject, Singleton}

import com.epickur.api.dao.UserDAO
import com.epickur.api.entities.User
import play.api.libs.json.JsObject
import reactivemongo.bson.BSONObjectID

import scala.concurrent.Future

@Singleton
class UserService @Inject()(userDAO: UserDAO) {

	def create(user: User): Future[Unit] = {
		user.id = Option.apply(BSONObjectID.generate.stringify)
		user.createdAt = Option.apply(LocalDateTime.now())
		user.updatedAt = Option.apply(LocalDateTime.now())
		userDAO.create(user)
	}

	def read(id: String): Future[List[JsObject]] = userDAO.read(id)

	def update(user: User): Future[Unit] = {
		user.password = null
		user.allow = Option.empty
		user.code = Option.empty
		user.key = Option.empty
		user.newPassword = Option.empty
		user.createdAt = Option.empty
		user.updatedAt = Option.apply(LocalDateTime.now())
		userDAO.update(user)
	}
}
