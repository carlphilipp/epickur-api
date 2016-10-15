package com.epickur.api.controllers

import javax.inject.{Inject, Singleton}

import com.epickur.api.entities.Caterer
import com.epickur.api.services.CatererService
import play.Logger
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json.{Json, OWrites, Reads}
import play.api.mvc.Action

import scala.concurrent.Future

@Singleton
class CatererController @Inject()(catererService: CatererService) extends Controller {

	implicit val catererToJson: OWrites[Caterer] = Caterer.catererToJsonWeb
	implicit val jsonToCaterer: Reads[Caterer] = Caterer.jsonToCatererWeb

	def create = Action.async(parse.json) { request =>
		request.body.validate[Caterer].map { caterer =>
			catererService.create(caterer)
				.map(Unit => {
					// TODO generate code and send an email in an async way
					Redirect(routes.CatererController.read(caterer.id.get))
				})
				.recover(handleRecover(caterer, "creating"))
		}.recoverTotal(e => handleTotalRecover(e))
	}

	def read(id: String) = Action.async { request =>
		catererService.read(id)
			.map(caterers => {
				if (caterers.nonEmpty)
					Ok(Json.toJson(caterers.head.as[Caterer]))
				else
					NotFound
			})
			.recover(handleRecover(id, "getting"))
	}

	def update(id: String) = Action.async(parse.json) { request =>
		request.body.validate[Caterer].map { caterer =>
			if (validateCaterer(caterer, id)) {
				caterer.id = Option.apply(id)
				catererService.update(caterer)
					.map(Unit => Redirect(routes.CatererController.read(caterer.id.get)))
					.recover(handleRecover(id, "updating"))
			} else {
				Future(BadRequest)
			}
		}.recoverTotal(e => handleTotalRecover(e))
	}

	private def validateCaterer(user: Caterer, id: String): Boolean = user.id.getOrElse(id) == id
}

