package com.epickur.api.utils

import com.epickur.api.entities.{Role, User}
import play.api.libs.json._
import reactivemongo.bson.BSONObjectID

object UserUtils {

	implicit val jsonToUser: Reads[User] = User.jsonToUserWeb

	val id = BSONObjectID.generate.stringify
	val name = "carlphilipp"
	val first = "carl"
	val last = "harmant"
	val password = "mypassword"
	val email = "cp.harmant@gmail.com"
	val countryCode = "1"
	val nationalNumber = "3128411111"
	val zipCode = "60614"
	val state = "Illinois"
	val country = "USA"
	val allow = false
	val createdAt = "2016-10-01T19:41:00.683"
	val updatedAt = "2016-10-01T19:41:00.683"

	val userAsString = s"""{"id":"$id","name":"$name","first":"$first","last":"$last","password":"$password","email":"$email","phoneNumber":{"nationalNumber":$nationalNumber,"countryCode":$countryCode},"zipCode":"$zipCode","state":"$state","country":"$country","allow":$allow,"createdAt":"$createdAt","updatedAt":"$updatedAt"}"""
	val user = Json.parse(userAsString).as[User]

	def verifyUser(user: User) = {
		assert(user.id.isDefined)
		assert(user.name == UserUtils.name)
		assert(user.first == UserUtils.first)
		assert(user.last == UserUtils.last)
		assert(user.password != null)
		assert(user.password != UserUtils.password)
		assert(user.email == UserUtils.email)
		assert(user.zipCode == UserUtils.zipCode)
		assert(user.state == UserUtils.state)
		assert(user.country == UserUtils.country)
		assert(user.role.isDefined)
		assert(user.role.get == Role.user)
		assert(user.allow.isDefined)
		assert(!user.allow.get)
		assert(user.createdAt != null)
		assert(user.updatedAt != null)
	}
}
