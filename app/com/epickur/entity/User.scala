package com.epickur.entity

import play.api.libs.json.{Json, Writes}

class User {
	private[this] var mName: String = _
	private[this] var mFirst: String = _

	def name = mName

	def first = mFirst

	def name_=(s: String) = {
		mName = s
	}

	def first_=(a: String) = {
		mFirst = a
	}
}

object User {
	implicit val userToJson: Writes[User] = Writes {
		(user: User) => Json.obj("first" -> user.first, "name" -> user.name)
	}
}
