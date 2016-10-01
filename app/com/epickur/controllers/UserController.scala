package com.epickur.controllers

import javax.inject.{Inject, Singleton}

import com.epickur.entity.User
import play.api.libs.json._
import play.api.mvc.{Action, Controller}

@Singleton
class UserController @Inject() extends Controller {

	def getUser = Action {
		val user = new User("carlphilipp", "carl", "harmant", "mypassword", "cp.harmant@gmail.com", "60614", "Illinois", "USA")
		Ok(Json.toJson(user))
	}
}
