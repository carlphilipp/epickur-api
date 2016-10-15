package com.epickur.api.services

import java.time.LocalDateTime
import javax.inject.{Inject, Singleton}

import com.epickur.api.dao.CatererDAO
import com.epickur.api.entities.Caterer
import play.api.libs.json.JsObject
import reactivemongo.bson.BSONObjectID

import scala.concurrent.Future

@Singleton
class CatererService @Inject()(catererDAO: CatererDAO) {

	def create(caterer: Caterer): Future[Unit] = {
		caterer.id = Option.apply(BSONObjectID.generate.stringify)
		caterer.createdAt = Option.apply(LocalDateTime.now())
		caterer.updatedAt = Option.apply(LocalDateTime.now())
		catererDAO.create(caterer)
	}

	def read(id: String): Future[List[JsObject]] = catererDAO.read(id)

	def update(caterer: Caterer): Future[Unit] = {
		catererDAO.update(caterer)
	}
}
