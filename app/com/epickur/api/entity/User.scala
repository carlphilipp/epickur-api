package com.epickur.api.entity

import java.time.LocalDateTime

import play.api.libs.json.Json

case class User(var id: Option[Long] = None,
				var name: String,
				var first: String,
				var last: String,
				var password: String,
				var email: String,
				var zipcode: String,
				var state: String,
				var country: String,
				var allow: Int = 0,
				var code: Option[String] = None,
				var key: Option[String] = None,
				var newPassword: Option[String] = None,
				var createdAt: LocalDateTime = LocalDateTime.now(),
				var updatedAt: LocalDateTime = LocalDateTime.now())

object User {
	implicit val userToJson = Json.writes[User]
	implicit val jsonToUser = Json.reads[User]
}
