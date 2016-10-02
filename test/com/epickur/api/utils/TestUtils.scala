package com.epickur.api.utils

import com.epickur.api.entity.User
import play.api.libs.json.Json

object TestUtils {
	val id = "123787891"
	val name = "carlphilipp"
	val first = "carl"
	val last = "harmant"
	val password = "mypassword"
	val email = "cp.harmant@gmail.com"
	val zipcode = "60614"
	val state = "Illinois"
	val country = "USA"
	val allow = "0"
	val createdAt = "2016-10-01T19:41:00.683"
	val updatedAt = "2016-10-01T19:41:00.683"

	val userAsString = s"""{"id":$id,"name":"$name","first":"$first","last":"$last","password":"$password","email":"$email","zipcode":"$zipcode","state":"$state","country":"$country","allow":$allow,"createdAt":"$createdAt","updatedAt":"$updatedAt"}"""
	val user = Json.parse(userAsString).as[User]

	def verifyUser(user: User) = {
		assert(user.id.get == TestUtils.id.toLong)
		assert(user.name == TestUtils.name)
		assert(user.first == TestUtils.first)
		assert(user.last == TestUtils.last)
		assert(user.password == TestUtils.password)
		assert(user.email == TestUtils.email)
		assert(user.zipcode == TestUtils.zipcode)
		assert(user.state == TestUtils.state)
		assert(user.country == TestUtils.country)
		assert(user.allow == TestUtils.allow.toInt)
		assert(user.createdAt != null)
		assert(user.updatedAt != null)
	}
}
