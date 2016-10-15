package com.epickur.api.dao

import javax.inject.{Inject, Singleton}

import com.epickur.api.entities.Caterer
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json.{Json, OWrites}
import play.modules.reactivemongo.json.JsObjectDocumentWriter
import play.modules.reactivemongo.ReactiveMongoApi

import scala.concurrent.Future

@Singleton
class CatererDAO @Inject()(val reactiveMongoApi: ReactiveMongoApi) extends DAO[Caterer] {

	implicit val userToJson: OWrites[Caterer] = Caterer.catererToJsonDB

	override val collection = loadCollection("caterers")

	override def create(caterer: Caterer): Future[Unit] = {
		collection.flatMap(_.insert(caterer)).map(_ => {})
	}

	override def update(caterer: Caterer): Future[Unit] = {
		collection.flatMap(_.update(Json.obj("_id" -> caterer.id.get), Caterer.getJsonUpdatedCaterer(caterer))).map(_ => {})
	}
}
