package com.epickur.api.dao

import javax.inject.{Inject, Singleton}

import com.epickur.api.entities.User
import play.modules.reactivemongo.{MongoController, ReactiveMongoApi, ReactiveMongoComponents}
import reactivemongo.play.json.collection.JSONCollection

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class UserDAO @Inject()(val reactiveMongoApi: ReactiveMongoApi)(implicit exec: ExecutionContext) extends MongoController with ReactiveMongoComponents {

	def userFuture: Future[JSONCollection] = database.map(_.collection[JSONCollection]("users"))

	def create(user: User): Future[Unit] = userFuture.flatMap(_.insert(user)).map(_ => {})

	def read(id: Long): User = {
		new User(Option.apply(id), "carlphilipp", "carl", "harmant", "mypassword", "cp.harmant@gmail.com", "60614", "Illinois", "USA")
	}

	def update(user: User): User = {
		user
	}
}
