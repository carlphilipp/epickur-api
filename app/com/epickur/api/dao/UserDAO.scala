package com.epickur.api.dao

import javax.inject.{Inject, Singleton}

import com.epickur.api.entities.User
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json.{Json, OWrites}
import play.modules.reactivemongo.json.JsObjectDocumentWriter
import play.modules.reactivemongo.ReactiveMongoApi

import scala.concurrent.Future

@Singleton
class UserDAO @Inject()(val reactiveMongoApi: ReactiveMongoApi) extends DAO[User] {

	implicit val userToJson: OWrites[User] = User.userToJsonDB

	override val collection = loadCollection("users")

	override def create(user: User): Future[Unit] = {
		collection.flatMap(_.insert(user)).map(_ => {})
	}

	override def update(user: User): Future[Unit] = {
		collection.flatMap(_.update(Json.obj("_id" -> user.id.get), User.getJsonUpdatedUser(user))).map(_ => {})
	}
}
