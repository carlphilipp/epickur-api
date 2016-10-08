package com.epickur.api.controllers

import javax.inject.{Inject, Singleton}

import com.epickur.api.entities.User
import com.epickur.api.services.UserService
import play.api.Logger
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json.{JsError, Json, OWrites, Reads}
import play.api.mvc.{Action, Controller, Result}
import reactivemongo.core.actors.Exceptions.NodeSetNotReachable
import reactivemongo.core.errors.DatabaseException

import scala.concurrent.Future

@Singleton
class UserController @Inject()(userService: UserService) extends Controller {

	implicit val errorMessageToJson: OWrites[ErrorMessage] = Json.writes[ErrorMessage]
	implicit val userToJson: OWrites[User] = User.userToJsonWeb
	implicit val jsonToUser: Reads[User] = User.jsonToUserWeb

	def create = Action.async(parse.json) { request =>
		request.body.validate[User].map { user =>
			userService.create(user)
				.map(Unit => Redirect(routes.UserController.read(user.id.get)))
				.recover(handleRecover(user, "creating"))
		}.recoverTotal(e => handleTotalRecover(e))
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
		request.body.validate[User].map{ user =>
			if (validateUser(user, id)) {
				user.id = Option.apply(id)
				userService.update(user)
					.map(Unit => Redirect(routes.UserController.read(user.id.get)))
					.recover(handleRecover(id, "updating"))
			} else {
				Future(BadRequest)
			}
		}.recoverTotal(e => handleTotalRecover(e))
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

	private def handleTotalRecover(jsError: JsError): Future[Result] = {
		Logger.error(s"Error while converting to object: $jsError")
		Future(BadRequest(Json.toJson(ErrorMessage(jsError.errors.head._2.head.message))))
	}

	private def validateUser(user: User, id: String): Boolean = user.id.getOrElse(id) == id
}

case class ErrorMessage(var message: String)

