package com.epickur.api.entities

import java.time.LocalDateTime

import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber
import play.api.libs.json.{JsObject, Json, OWrites}

case class Caterer(var id: Option[String] = None,
				   var name: String,
				   var description: String,
				   var manager: String,
				   var email: String,
				   var phoneNumber: PhoneNumber,
				   var location: Location,
				   var workingTimes: WorkingTimes,
				   var createdBy: String,
				   var createdAt: Option[LocalDateTime] = None,
				   var updatedAt: Option[LocalDateTime] = None)

case class Location(var address: Address, var geo: Geo)

case class Address(var label: String,
				   var houseNumber: String,
				   var street: String,
				   var city: String,
				   var postalCode: Int,
				   var state: String,
				   var country: String)

case class Geo(var typeOf: String, var coordinates: Array[Double])

case class WorkingTimes(var hours: Hours,
						var minimumPreparationTime: Int)

case class Hours(var mon: Seq[TimeFrame],
				 var tue: Seq[TimeFrame],
				 var wed: Seq[TimeFrame],
				 var thu: Seq[TimeFrame],
				 var fri: Seq[TimeFrame],
				 var sat: Seq[TimeFrame],
				 var sun: Seq[TimeFrame])

case class TimeFrame(var open: Int, var close: Int)

object Caterer {
	val catererToJsonDB: OWrites[Caterer] = new OWrites[Caterer] {
		def writes(caterer: Caterer): JsObject = generateJsonForCaterer(caterer)
	}

	def getJsonUpdatedCaterer(caterer: Caterer): JsObject = {
		Json.obj("$set" -> generateJsonForCaterer(caterer).-("createdAt"))
	}

	private def generateJsonForCaterer(caterer: Caterer): JsObject = {
		Json.obj(
			"_id" -> caterer.id,
			"description" -> caterer.description,
			"manager" -> caterer.manager,
			"email" -> caterer.email,
			//"phoneNumber" -> Json.toJson(caterer.phoneNumber),
			//"location" -> Json.toJson(caterer.location),
			//"workingTimes" -> Json.toJson(caterer.workingTimes),
			"createdBy" -> caterer.createdBy,
			"createdAt" -> Json.toJson(caterer.createdAt.get),
			"updatedAt" -> Json.toJson(caterer.updatedAt.get)
		)
	}
}
