package com.epickur.api.utils

import com.epickur.api.entities.Caterer
import play.api.libs.json.{Json, Reads}
import reactivemongo.bson.BSONObjectID

object CatererUtils {

	implicit val jsonToCaterer: Reads[Caterer] = Caterer.jsonToCatererWeb

	val id = BSONObjectID.generate.stringify
	val name = "Chez moi"
	val description = "French restaurant"
	val manager = "Francis"
	val email = "francis@gmail.com"
	val phoneNumberCode = 1
	val phoneNumberNationalNumber = 3128474457L

	val catererAsString =
		s"""{"id":"$id",
		"name":"$name",
	  	"description":"$description",
	  	"manager":"$manager",
	  	"email":"$email",
	  	"phoneNumber": {
	    	"countryCode":$phoneNumberCode,
	    	"nationalNumber":$phoneNumberNationalNumber
	  	},
	  	"location":{
	    	"address":{"label":"derp","houseNumber":"35","street":"Wrigthweeee","city":"Chicagi","postalCode":60614,"state":"Illnosi","country":"USA"},
	    	"geo":{"type":"Point","coordinates":[25.2,54.7]}
	  	},
	  	"workingTimes":{
	    	"hours":{
	      		"mon":[{"open":45648,"close":45648}],
				"tue":[{"open":45648,"close":45648}],
	      		"wed":[{"open":45648,"close":45648}],
	      		"thu":[{"open":45648,"close":45648}],
	      		"fri":[{"open":45648,"close":45648}],
	      		"sat":[{"open":45648,"close":45648}],
	      		"sun":[{"open":45648,"close":45648}]},
	    	"minimumPreparationTime":5
	  	},
	  	"createdBy":"580196700b00008700013aec",
	  	"createdAt":"2016-10-15T13:18:38.81",
	  	"updatedAt":"2016-10-15T13:18:38.81"
		}
  """

	val caterer = Json.parse(catererAsString).as[Caterer]

	def verifyCaterer(caterer: Caterer) = {
		assert(caterer.id.isDefined)
		assert(caterer.name == CatererUtils.name)
		assert(caterer.description == CatererUtils.description)
		assert(caterer.manager == CatererUtils.manager)
		assert(caterer.email == CatererUtils.email)
		assert(caterer.phoneNumber.getCountryCode == CatererUtils.phoneNumberCode)
		assert(caterer.phoneNumber.getNationalNumber == CatererUtils.phoneNumberNationalNumber.toLong)
		assert(caterer.createdAt != null)
		assert(caterer.updatedAt != null)
	}
}
