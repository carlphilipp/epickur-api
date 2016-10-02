package com.epickur.controllers

import javax.inject.{Inject, Singleton}

import com.epickur.entity.User
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}

@Singleton
class UserController @Inject() extends Controller {

	def create = Action(parse.json) { request =>
		val body = Json.parse(request.body.toString())
		val user = body.as[User]
		Ok(Json.toJson(user))
	}

	def read(id: Long) = Action {
		val user = new User(Option.apply(id), "carlphilipp", "carl", "harmant", "mypassword", "cp.harmant@gmail.com", "60614", "Illinois", "USA")
		Ok(Json.toJson(user))
	}
}
