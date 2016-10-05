package com.epickur.api.services

import javax.inject.{Inject, Singleton}

import com.epickur.api.dao.UserDAO
import com.epickur.api.entities.User
import play.api.libs.json.{JsObject, Json}

import scala.concurrent.Future

@Singleton
class UserService @Inject()(userDAO: UserDAO) {

	def create(user: User): Future[Unit] = userDAO.create(user)

	def read(id: Long): User = userDAO.read(id)

	def readByName(name: String): Future[List[JsObject]] = userDAO.readByName(name)

	def update(user: User): User = userDAO.update(user)
}
