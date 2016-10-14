package com.epickur.api.dao

import javax.inject.{Inject, Singleton}

import com.epickur.api.entities.User
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json.{JsObject, Json, OWrites}
import play.modules.reactivemongo.ReactiveMongoApi
import play.modules.reactivemongo.json._
import reactivemongo.api.ReadPreference

import scala.concurrent.Future

@Singleton
class UserDAO @Inject()(val reactiveMongoApi: ReactiveMongoApi) extends DAO[User] {

	implicit val userToJson: OWrites[User] = User.userToJsonDB

	def collection = loadCollection("users")

	override def create(user: User): Future[Unit] = {
		collection.flatMap(_.insert(user)).map(_ => {})
	}

	override def read(id: String): Future[List[JsObject]] = {
		collection.flatMap(_.find(Json.obj("_id" -> id)).cursor[JsObject](ReadPreference.primary).collect[List](1))
	}

	override def update(user: User): Future[Unit] = {
		collection.flatMap(_.update(Json.obj("_id" -> user.id.get), User.getJsonUpdatedUser(user))).map(_ => {})
	}
}
