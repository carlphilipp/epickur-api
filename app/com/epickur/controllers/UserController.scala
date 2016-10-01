package com.epickur.controllers

import javax.inject.{Inject, Singleton}

import com.epickur.entity.User
import play.api.libs.json._
import play.api.mvc.{Action, Controller}

@Singleton
class UserController @Inject() extends Controller {

	def getUser = Action {
		val user = new User()
		user.name_=("harmant")
		user.first_=("carl")
		Ok(Json.toJson(user))
	}
}
