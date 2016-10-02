package com.epickur.api.dao

import javax.inject.Singleton

import com.epickur.api.entities.User

@Singleton
class UserDAO {

	def create(user: User): User = {
		user
	}

	def read(id: Long): User = {
		new User(Option.apply(id), "carlphilipp", "carl", "harmant", "mypassword", "cp.harmant@gmail.com", "60614", "Illinois", "USA")
	}

	def update(user: User): User = {
		user
	}
}
