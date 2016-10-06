package com.epickur.api.dao

import javax.inject.{Inject, Singleton}

import com.epickur.api.entities.User
import play.api.libs.json.{JsObject, Json, OWrites}
import play.modules.reactivemongo.json._
import play.modules.reactivemongo.{MongoController, ReactiveMongoApi, ReactiveMongoComponents}
import reactivemongo.api.ReadPreference
import reactivemongo.play.json.collection.JSONCollection

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class UserDAO @Inject()(val reactiveMongoApi: ReactiveMongoApi)(implicit exec: ExecutionContext) extends MongoController with ReactiveMongoComponents {

	implicit val userToJson: OWrites[User] = User.userToJsonDB

	def userCollection: Future[JSONCollection] = database.map(_.collection[JSONCollection]("users"))

	def create(user: User): Future[Unit] = userCollection.flatMap(_.insert(user)).map(_ => {})

	def read(id: String): Future[List[JsObject]] = userCollection.flatMap(_.find(Json.obj("_id" -> id)).cursor[JsObject](ReadPreference.primary).collect[List](1))

	def update(user: User): Future[Unit] = userCollection.flatMap(_.update(Json.obj("_id" -> user.id.get), User.getJsonUpdatedUser(user))).map(_ => {})
}
