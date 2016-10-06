package com.epickur.api.controllers

import javax.inject.{Inject, Singleton}

import com.epickur.api.entities.User
import com.epickur.api.services.UserService
import play.api.Logger
import play.api.libs.json.{Json, OWrites, Reads}
import play.api.mvc.{Action, Controller}
import reactivemongo.core.actors.Exceptions.NodeSetNotReachable
import reactivemongo.core.errors.DatabaseException

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class UserController @Inject()(userService: UserService)(implicit exec: ExecutionContext) extends Controller {

	implicit val userToJson: OWrites[User] = User.userToJsonWeb
	implicit val jsonToUser: Reads[User] = User.jsonToUserWeb

	def create = Action.async(parse.json) { request =>
		val user = request.body.as[User]
		Logger.debug("Create user: " + user)
		userService.create(user)
			.map(Unit => Redirect(routes.UserController.read(user.id.get)))
			.recover(handleRecover(user, "creating"))
	}

	def read(id: String) = Action.async { request =>
		userService.read(id)
			.map(users => {
				if (users.nonEmpty)
					Ok(Json.toJson(users.head.as[User]))
				else
					NotFound
			})
			.recover(handleRecover(id, "getting"))
	}

	def update(id: String) = Action.async(parse.json) { request =>
		val user = request.body.as[User]
		if (validateUser(user, id)) {
			user.id = Option.apply(id)
			userService.update(user)
				.map(Unit => Redirect(routes.UserController.read(user.id.get)))
				.recover(handleRecover(id, "updating"))
		} else {
			Future(BadRequest)
		}
	}

	private def handleRecover(obj: Any, log: String): PartialFunction[Throwable, Status] = {
		case dbe: DatabaseException =>
			Logger.error(s"Error while $log user $obj", dbe)
			Conflict
		case n: NodeSetNotReachable =>
			Logger.error(s"Error while $log user $obj", n)
			ServiceUnavailable
		case t: Throwable =>
			Logger.error(s"Error while $log user $obj", t)
			ServiceUnavailable
	}

	private def validateUser(user: User, id: String) = user.id.getOrElse(id) == id
}
