package com.epickur.api.entities

import java.time.LocalDateTime

import com.epickur.api.entities.Role.Role
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber
import play.api.libs.json._

case class User(var id: Option[String] = None,
				var name: String,
				var first: String,
				var last: String,
				var password: String,
				var email: String,
				var phoneNumber: PhoneNumber,
				var zipCode: String,
				var state: String,
				var country: String,
				var role: Option[Role],
				var allow: Option[Boolean] = None,
				var code: Option[String] = None,
				var key: Option[String] = None,
				var newPassword: Option[String] = None,
				var createdAt: Option[LocalDateTime] = None,
				var updatedAt: Option[LocalDateTime] = None)

object Role extends Enumeration {
	type Role = Value
	val admin, user, super_user, epickur_web = Value
}

object User {
	// TODO probably move that function somewhere else
	def enumReads[E <: Enumeration](enum: E): Reads[E#Value] = new Reads[E#Value] {
		def reads(json: JsValue): JsResult[E#Value] = json match {
			case JsString(s) =>
				try {
					JsSuccess(enum.withName(s.toLowerCase))
				} catch {
					case _: NoSuchElementException => JsError(s"Enumeration expected of type: '${enum.getClass}', but it does not appear to contain the value: '$s'")
				}
			case _ => JsError("String value expected")
		}
	}

	implicit val jsonToRole: Reads[Role.Value] = enumReads(Role)

	implicit val phoneNumberToJson: OWrites[PhoneNumber] = new OWrites[PhoneNumber] {
		def writes(phoneNumber: PhoneNumber): JsObject = {
			Json.obj(
				"countryCode" -> phoneNumber.getCountryCode,
				"nationalNumber" -> phoneNumber.getNationalNumber
			)
		}
	}
	implicit val jsonToPhoneNumber: Reads[PhoneNumber] = new Reads[PhoneNumber] {
		def reads(json: JsValue): JsResult[PhoneNumber] = {
			for {
				countryCode <- (json \ "countryCode").validate[Int]
				nationalNumber <- (json \ "nationalNumber").validate[Long]
			} yield {
				val phoneNumber = new PhoneNumber()
				phoneNumber.setCountryCode(countryCode)
				phoneNumber.setNationalNumber(nationalNumber)
				phoneNumber
			}
		}
	}
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
				phoneNumber <- (json \ "phoneNumber").validate[PhoneNumber]
				zipCode <- (json \ "zipCode").validate[String]
				state <- (json \ "state").validate[String]
				country <- (json \ "country").validate[String]
				role <- (json \ "role").validateOpt[Role]
				allow <- (json \ "allow").validateOpt[Boolean]
				code <- (json \ "code").validateOpt[String]
				key <- (json \ "key").validateOpt[String]
				newPassword <- (json \ "newPassword").validateOpt[String]
				createdAt <- (json \ "createdAt").validateOpt[LocalDateTime]
				updatedAt <- (json \ "updatedAt").validateOpt[LocalDateTime]
			} yield {
				new User(id, name, first, last, password, email, phoneNumber, zipCode, state, country, role, allow,
					code, key, newPassword, createdAt, updatedAt)
			}
		}
	}

	val userToJsonDB: OWrites[User] = new OWrites[User] {
		def writes(user: User) = generateJsonForUser(user)
	}

	def getJsonUpdatedUser(user: User): JsObject = {
		Json.obj("$set" -> generateJsonForUser(user).-("createdAt"))
	}

	private def generateJsonForUser(user: User): JsObject = {
		// TODO it gotta have a better way to do that
		var json = Json.obj(
			"_id" -> user.id,
			"name" -> user.name,
			"first" -> user.first,
			"last" -> user.last)
		if (user.password != null)
			json = json + ("password" -> Json.toJson(user.password))
		json = json + ("email" -> Json.toJson(user.email))
		json = json + ("phoneNumber" -> Json.toJson(user.phoneNumber))
		json = json + ("zipCode" -> Json.toJson(user.zipCode))
		json = json + ("state" -> Json.toJson(user.state))
		json = json + ("country" -> Json.toJson(user.country))
		if (user.role.isDefined)
			json = json + ("role" -> Json.toJson(user.role))
		if (user.allow.isDefined)
			json = json + ("allow" -> Json.toJson(user.allow.get))
		if (user.code.isDefined)
			json = json + ("code" -> Json.toJson(user.code.get))
		if (user.key.isDefined)
			json = json + ("key" -> Json.toJson(user.key.get))
		if (user.newPassword.isDefined)
			json = json + ("newPassword" -> Json.toJson(user.newPassword.get))
		if (user.createdAt.isDefined)
			json = json + ("createdAt" -> Json.toJson(user.createdAt.get))
		if (user.updatedAt.isDefined)
			json = json + ("updatedAt" -> Json.toJson(user.updatedAt.get))
		json
	}

	//implicit val jsonToUser = Json.reads[User]
}
