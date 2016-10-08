package com.epickur.api.utils

import java.security.SecureRandom

import com.roundeights.hasher.Implicits._

class PasswordManager(private val password: String) {

	// TODO redesign
	private val passwordHashed = password.sha512.hex
	private val saltHashed = new SecureRandom().toString.sha512.hex
	private val encodedPasswordSalt = (passwordHashed + saltHashed).sha512.hex

	def createDBPassword: String = {
		saltHashed + encodedPasswordSalt
	}

	def getTemporaryRegistrationCode(name: String, email: String): String = {
		(name + createDBPassword + email).sha512.hex
	}
}
