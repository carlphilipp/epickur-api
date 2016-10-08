package com.epickur.api.services

import java.time.LocalDateTime
import javax.inject.{Inject, Singleton}

import com.epickur.api.dao.UserDAO
import com.epickur.api.entities.{Role, User}
import com.epickur.api.utils.PasswordManager
import play.api.libs.json.JsObject
import reactivemongo.bson.BSONObjectID

import scala.concurrent.Future

@Singleton
class UserService @Inject()(userDAO: UserDAO) {

	def create(user: User): Future[Unit] = {
		user.id = Option.apply(BSONObjectID.generate.stringify)
		user.role = Option.apply(Role.user)
		user.allow = Option.apply(false)
		user.createdAt = Option.apply(LocalDateTime.now())
		user.updatedAt = Option.apply(LocalDateTime.now())

		val passwordManager = new PasswordManager(user.password)
		user.password = passwordManager.createDBPassword
		val temporaryCode = passwordManager.getTemporaryRegistrationCode(user.name, user.email)
		// TODO Send email to user containing temp code

		userDAO.create(user)
	}

	def read(id: String): Future[List[JsObject]] = userDAO.read(id)

	def update(user: User): Future[Unit] = {
		user.password = null
		user.role = Option.empty
		user.allow = Option.empty
		user.key = Option.empty
		user.newPassword = Option.empty
		user.createdAt = Option.empty
		user.updatedAt = Option.apply(LocalDateTime.now())
		userDAO.update(user)
	}
}
