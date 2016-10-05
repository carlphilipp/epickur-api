package com.epickur.api.entities

import java.time.LocalDateTime

import play.api.libs.json.Reads._
import play.api.libs.json._

case class User(var id: Option[Long] = None,
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
				var createdAt: LocalDateTime = LocalDateTime.now(),
				var updatedAt: LocalDateTime = LocalDateTime.now())

object User {

	implicit val jsonToUser: Reads[User] = new Reads[User] {
		def reads(json: JsValue): JsResult[User] = {
			for {
				id <- (json \ "id").validateOpt[Long]
				name <- (json \ "name").validate[String]
				first <- (json \ "first").validate[String]
				last <- (json \ "last").validate[String]
				password <- (json \ "password").validate[String]
				email <- (json \ "email").validate[String]
				zipCode <- (json \ "zipcode").validate[String]
				state <- (json \ "state").validate[String]
				country <- (json \ "country").validate[String]
				allow <- (json \ "allow").validateOpt[Int]
				code <- (json \ "code").validateOpt[String]
				key <- (json \ "key").validateOpt[String]
				newPassword <- (json \ "newPassword").validateOpt[String]
				createdAt <- (json \ "createdAt").validate[LocalDateTime]
				updatedAt <- (json \ "updatedAt").validate[LocalDateTime]
			} yield new User(id, name, first, last, password, email, zipCode, state, country, allow, code, key, newPassword, createdAt, updatedAt)
		}
	}

	implicit val userToJson = new OWrites[User] {
		def writes(user: User) = Json.obj(
			"_id" -> user.id,
			"name" -> user.name,
			"first" -> user.first,
			"password" -> user.password,
			"email" -> user.email,
			"zipcode" -> user.zipCode,
			"state" -> user.state,
			"country" -> user.country,
			"allow" -> user.allow,
			"code" -> user.code,
			"key" -> user.key,
			"newPassword" -> user.newPassword,
			"createdAt" -> user.createdAt,
			"updatedAt" -> user.updatedAt
		)
	}
	//implicit val userToJson = Json.writes[User]
	//implicit val jsonToUser = Json.reads[User]
}
