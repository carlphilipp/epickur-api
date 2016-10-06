package com.epickur.api.entities

import java.time.LocalDateTime

import play.api.libs.json.Reads._
import play.api.libs.json._

case class User(var id: Option[String] = None,
				var name: String,
				var first: String,
				var last: String,
				var password: String,
				var email: String,
				var zipCode: String,
				var state: String,
				var country: String,
				var allow: Option[Int] = None,
				var code: Option[String] = None,
				var key: Option[String] = None,
				var newPassword: Option[String] = None,
				var createdAt: Option[LocalDateTime] = None,
				var updatedAt: Option[LocalDateTime] = None)

object User {
	val userToJsonWeb: OWrites[User] = Json.writes[User]
	val jsonToUserWeb: Reads[User] = new Reads[User] {
		def reads(json: JsValue): JsResult[User] = {
			for {
				id <- {
					val id = (json \ "id").validateOpt[String]
					if (id.isSuccess && id.get.isDefined) id else (json \ "_id").validateOpt[String]
				}
				name <- (json \ "name").validate[String]
				first <- (json \ "first").validate[String]
				last <- (json \ "last").validate[String]
				password <- (json \ "password").validate[String]
				email <- (json \ "email").validate[String]
				zipCode <- (json \ "zipCode").validate[String]
				state <- (json \ "state").validate[String]
				country <- (json \ "country").validate[String]
				allow <- (json \ "allow").validateOpt[Int]
				code <- (json \ "code").validateOpt[String]
				key <- (json \ "key").validateOpt[String]
				newPassword <- (json \ "newPassword").validateOpt[String]
				createdAt <- (json \ "createdAt").validateOpt[LocalDateTime]
				updatedAt <- (json \ "updatedAt").validateOpt[LocalDateTime]
			} yield new User(id, name, first, last, password, email, zipCode, state, country, allow, code, key, newPassword, createdAt, updatedAt)
		}
	}

	val userToJsonDB: OWrites[User] = new OWrites[User] {
		def writes(user: User) = {
			val result = Json.obj(
				"_id" -> user.id,
				"name" -> user.name,
				"first" -> user.first,
				"last" -> user.last,
				"password" -> user.password,
				"email" -> user.email,
				"zipCode" -> user.zipCode,
				"state" -> user.state,
				"country" -> user.country,
				"createdAt" -> user.createdAt,
				"updatedAt" -> user.updatedAt
			)
			if (user.allow != null)
				result + ("allow" -> Json.toJson(user.allow))
			if (user.code != null)
				result + ("code" -> Json.toJson(user.code))
			if (user.key != null)
				result + ("key" -> Json.toJson(user.key))
			if (user.newPassword != null)
				result + ("newPassword" -> Json.toJson(user.newPassword))
			result
		}
	}
	//implicit val jsonToUser = Json.reads[User]
}
