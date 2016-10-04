package com.epickur.api.controllers

import javax.inject.{Inject, Singleton}

import com.epickur.api.entities.User
import com.epickur.api.services.UserService
import play.api.Logger
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}
import reactivemongo.core.actors.Exceptions.NodeSetNotReachable

import scala.concurrent.ExecutionContext

@Singleton
class UserController @Inject()(userService: UserService)(implicit exec: ExecutionContext) extends Controller {

	def create = Action.async(parse.json) { request =>
		val user = request.body.as[User]
		userService.create(user)
			.map(Unit => Created)
			.recover {
				case n: NodeSetNotReachable =>
					Logger.error("Error while creating user " + user, n)
					ServiceUnavailable
				case t: Throwable =>
					Logger.error("Error while creating user " + user, t)
					InternalServerError
			}
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
