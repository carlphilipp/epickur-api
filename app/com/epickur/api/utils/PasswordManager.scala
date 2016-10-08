package com.epickur.api.utils

import java.security.SecureRandom

import com.roundeights.hasher.Implicits._

class PasswordManager(private val password: String) {

	private val passwordHashed = password.sha256.hex
	private val saltHashed = new SecureRandom().toString.sha256.hex
	private val encodedPasswordSalt = (passwordHashed + saltHashed).sha256.hex

	def createDBPassword: String = {
		saltHashed + encodedPasswordSalt
	}

	def getTemporaryRegistrationCode(name: String, email: String): String = {
		(name + saltHashed + encodedPasswordSalt + email).sha256.hex
	}
}
