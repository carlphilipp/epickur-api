package com.epickur.controllers

import javax.inject.{Inject, Singleton}

import play.api.mvc.{Action, Controller}

@Singleton
class UserController @Inject() extends Controller {

	def index = Action {
		Ok(views.html.index("User controller"))
	}
}
