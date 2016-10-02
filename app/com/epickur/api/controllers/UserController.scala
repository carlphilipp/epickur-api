package com.epickur.api.controllers

import javax.inject.{Inject, Singleton}

import com.epickur.api.entity.User
import com.epickur.api.services.UserService
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}

@Singleton
class UserController @Inject()(userService: UserService) extends Controller {

	def create = Action(parse.json) { request =>
		val user = request.body.as[User]
		val userCreated = userService.create(user)
		Ok(Json.toJson(userCreated))
	}

	def read(id: Long) = Action {
		val user = userService.read(id)
		Ok(Json.toJson(user))
	}

	def update(id: Long) = Action(parse.json) { request =>
		val user = request.body.as[User]
		val userUpdated = userService.update(user)
		Ok(Json.toJson(userUpdated))
	}
}
