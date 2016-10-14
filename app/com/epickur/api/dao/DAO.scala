package com.epickur.api.dao

import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json.JsObject
import play.modules.reactivemongo.{MongoController, ReactiveMongoComponents}
import reactivemongo.play.json.collection.JSONCollection

import scala.concurrent.Future

trait DAO[T] extends MongoController with ReactiveMongoComponents {

	def loadCollection(collection: String): Future[JSONCollection] = {
		database.map(_.collection[JSONCollection](collection))
	}

	def create(entity: T): Future[Unit]

	def read(id: String): Future[List[JsObject]]

	def update(entity: T): Future[Unit]
}
