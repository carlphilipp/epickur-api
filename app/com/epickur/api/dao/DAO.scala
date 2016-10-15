package com.epickur.api.dao

import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json.{JsObject, Json}
import play.modules.reactivemongo.json.JsObjectDocumentWriter
import play.modules.reactivemongo.{MongoController, ReactiveMongoComponents}
import reactivemongo.api.ReadPreference
import reactivemongo.play.json.collection.JSONCollection

import scala.concurrent.Future

trait DAO[T] extends MongoController with ReactiveMongoComponents {

	val collection: Future[JSONCollection]

	def loadCollection(collection: String): Future[JSONCollection] = {
		database.map(_.collection[JSONCollection](collection))
	}

	def create(entity: T): Future[Unit]

	def read(id: String): Future[List[JsObject]] = {
		collection.flatMap(_.find(Json.obj("_id" -> id)).cursor[JsObject](ReadPreference.primary).collect[List](1))
	}

	def update(entity: T): Future[Unit]
}
