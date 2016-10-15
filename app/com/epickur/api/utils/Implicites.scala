package com.epickur.api.utils

import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber
import play.api.libs.json._

object Implicites {

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

	def phoneNumberToJson: OWrites[PhoneNumber] = new OWrites[PhoneNumber] {
		def writes(phoneNumber: PhoneNumber): JsObject = {
			Json.obj(
				"countryCode" -> phoneNumber.getCountryCode,
				"nationalNumber" -> phoneNumber.getNationalNumber
			)
		}
	}
	def jsonToPhoneNumber: Reads[PhoneNumber] = new Reads[PhoneNumber] {
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

}
