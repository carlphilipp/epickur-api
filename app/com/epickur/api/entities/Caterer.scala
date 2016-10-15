package com.epickur.api.entities

import java.time.LocalDateTime

import com.epickur.api.utils.Implicites
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber
import play.api.libs.json._

case class Caterer(var id: Option[String] = None,
				   var name: String,
				   var description: String,
				   var manager: String,
				   var email: String,
				   var phoneNumber: PhoneNumber,
				   //var location: Location,
				   //var workingTimes: WorkingTimes,
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
	implicit val phoneNumberToJson = Implicites.phoneNumberToJson
	implicit val jsonToPhoneNumber = Implicites.jsonToPhoneNumber
	val catererToJsonWeb: OWrites[Caterer] = Json.writes[Caterer]
	val jsonToCatererWeb: Reads[Caterer] = new Reads[Caterer] {
		def reads(json: JsValue): JsResult[Caterer] = {
			for {
				id <- {
					val id = (json \ "id").validateOpt[String]
					if (id.isSuccess && id.get.isDefined) id else (json \ "_id").validateOpt[String]
				}
				name <- (json \ "name").validate[String]
				description <- (json \ "description").validate[String]
				manager <- (json \ "manager").validate[String]
				email <- (json \ "email").validate[String]
				phoneNumber <- (json \ "phoneNumber").validate[PhoneNumber]
				//location <- (json \ "location").validate[Location]
				//workingTimes <- (json \ "workingTimes").validate[WorkingTimes]
				createdBy <- (json \ "createdBy").validate[String]
				createdAt <- (json \ "createdAt").validateOpt[LocalDateTime]
				updatedAt <- (json \ "updatedAt").validateOpt[LocalDateTime]
			} yield {
				new Caterer(id, name, description, manager, email, phoneNumber/*, location, workingTimes*/, createdBy, createdAt, updatedAt)
			}
		}
	}

	val catererToJsonDB: OWrites[Caterer] = new OWrites[Caterer] {
		def writes(caterer: Caterer): JsObject = generateJsonForCaterer(caterer)
	}

	def getJsonUpdatedCaterer(caterer: Caterer): JsObject = {
		Json.obj("$set" -> generateJsonForCaterer(caterer).-("createdAt"))
	}

	private def generateJsonForCaterer(caterer: Caterer): JsObject = {
		var json = Json.obj(
			"_id" -> caterer.id,
			"name" -> caterer.name,
			"description" -> caterer.description,
			"manager" -> caterer.manager,
			"email" -> caterer.email,
			"phoneNumber" -> Json.toJson(caterer.phoneNumber),
			//"location" -> Json.toJson(caterer.location),
			//"workingTimes" -> Json.toJson(caterer.workingTimes),
			"createdBy" -> caterer.createdBy
		)
		if (caterer.createdAt.isDefined)
			json = json + ("createdAt" -> Json.toJson(caterer.createdAt.get))
		if (caterer.updatedAt.isDefined)
			json = json + ("updatedAt" -> Json.toJson(caterer.updatedAt.get))
		json
	}
}
