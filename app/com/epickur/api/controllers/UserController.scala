package com.epickur.api.controllers

import javax.inject.{Inject, Singleton}

import com.epickur.api.entities.User
import com.epickur.api.services.UserService
import play.api.Logger
import play.api.libs.json.{Json, OWrites, Reads}
import play.api.mvc.{Action, Controller}
import reactivemongo.core.actors.Exceptions.NodeSetNotReachable
import reactivemongo.core.errors.DatabaseException

import scala.concurrent.ExecutionContext

@Singleton
class UserController @Inject()(userService: UserService)(implicit exec: ExecutionContext) extends Controller {

	implicit val userToJson: OWrites[User] = User.userToJsonWeb
	implicit val jsonToUser: Reads[User] = User.jsonToUserWeb

	def create = Action.async(parse.json) { request =>
		val user = request.body.as[User]
		Logger.debug("Create user: " + user)
		userService.create(user)
			.map(Unit => Redirect(routes.UserController.read(user.id.get)))
			.recover {
				case dbe: DatabaseException =>
					Logger.error("Error while creating user " + user, dbe)
					Conflict
				case n: NodeSetNotReachable =>
					Logger.error("Error while creating user " + user, n)
					ServiceUnavailable
				case t: Throwable =>
					Logger.error("Error while creating user " + user, t)
					ServiceUnavailable
			}
	}

	def read(id: String) = Action.async { request =>
		userService.read(id)
			.map(users => {
				if (users.nonEmpty)
					Ok(Json.toJson(users.head.as[User]))
				else
					NotFound
			})
			.recover {
				case dbe: DatabaseException =>
					Logger.error("Error while getting user " + id, dbe)
					Conflict
				case n: NodeSetNotReachable =>
					Logger.error("Error while getting user " + id, n)
					ServiceUnavailable
				case t: Throwable =>
					Logger.error("Error while getting user " + id, t)
					ServiceUnavailable
			}
	}

	def update(id: Long) = Action(parse.json) { request =>
		val user = request.body.as[User]
		val userUpdated = userService.update(user)
		Ok(Json.toJson(userUpdated))
	}
}
