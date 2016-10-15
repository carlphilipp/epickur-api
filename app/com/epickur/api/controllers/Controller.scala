package com.epickur.api.controllers

import play.api.Logger
import play.api.libs.json.{JsError, Json, OWrites}
import play.api.mvc.Result
import reactivemongo.core.actors.Exceptions.NodeSetNotReachable
import reactivemongo.core.errors.DatabaseException
import play.api.libs.concurrent.Execution.Implicits._

import scala.concurrent.Future

trait Controller extends play.api.mvc.Controller {

	implicit val errorMessageToJson: OWrites[ErrorMessage] = Json.writes[ErrorMessage]

	protected def handleRecover(obj: Any, log: String): PartialFunction[Throwable, Status] = {
		case dbe: DatabaseException =>
			Logger.error(s"Error while $log user $obj", dbe)
			Conflict
		case n: NodeSetNotReachable =>
			Logger.error(s"Error while $log user $obj", n)
			ServiceUnavailable
		case t: Throwable =>
			Logger.error(s"Error while $log user $obj", t)
			ServiceUnavailable
	}

	protected def handleTotalRecover(jsError: JsError): Future[Result] = {
		Logger.error(s"Error while converting to object: $jsError")
		Future(BadRequest(Json.toJson(ErrorMessage(jsError.errors.head._2.head.message))))
	}
}

case class ErrorMessage(var message: String)
