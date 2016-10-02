package com.epickur.api.services

import javax.inject.Singleton

import com.epickur.api.entity.User

@Singleton
class UserService {

	def create(user: User): User = {
		user
	}
}
