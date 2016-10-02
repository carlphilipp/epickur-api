package com.epickur.api.services

import javax.inject.{Inject, Singleton}

import com.epickur.api.dao.UserDAO
import com.epickur.api.entities.User

@Singleton
class UserService @Inject()(userDAO: UserDAO) {

	def create(user: User): User = {
		userDAO.create(user)
	}

	def read(id: Long): User = {
		userDAO.read(id)
	}

	def update(user: User): User = {
		userDAO.update(user)
	}
}
