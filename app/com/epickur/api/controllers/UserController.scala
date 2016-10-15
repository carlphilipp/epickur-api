package com.epickur.api.controllers

import javax.inject.{Inject, Singleton}

import com.epickur.api.entities.User
import com.epickur.api.services.UserService
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json.{Json, OWrites, Reads}
import play.api.mvc.Action

import scala.concurrent.Future

@Singleton
class UserController @Inject()(userService: UserService) extends Controller {


	implicit val userToJson: OWrites[User] = User.userToJsonWeb
	implicit val jsonToUser: Reads[User] = User.jsonToUserWeb

	def create = Action.async(parse.json) { request =>
		request.body.validate[User].map { user =>
			userService.create(user)
				.map(Unit => {
					// TODO generate code and send an email in an async way
					Redirect(routes.UserController.read(user.id.get))
				})
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
		request.body.validate[User].map { user =>
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

	private def validateUser(user: User, id: String): Boolean = user.id.getOrElse(id) == id
}

