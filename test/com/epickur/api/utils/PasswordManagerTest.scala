package com.epickur.api.utils

import org.scalatest.{FeatureSpec, GivenWhenThen}

class PasswordManagerTest extends FeatureSpec with GivenWhenThen {

	info("Password manager password and temporary code handler")

	feature("Password manager") {
		scenario("Create a new password") {
			Given("a password")
			val password = "mypassword"

			When("asking for a SHA-256 encoded hash")
			val passwordManager = new PasswordManager(password)

			Then("should obtain the password encoded")
			val hashPassword = passwordManager.createDBPassword
			assert(hashPassword != null)

			Then("should obtain the password encoded")
			val temporaryCode = passwordManager.getTemporaryRegistrationCode("carl", "carl@bemychef.com.au")
			assert(temporaryCode != null)
		}
	}
}
