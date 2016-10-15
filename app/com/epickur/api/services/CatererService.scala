package com.epickur.api.services

import javax.inject.{Inject, Singleton}

import com.epickur.api.dao.CatererDAO
import com.epickur.api.entities.Caterer
import play.api.libs.json.JsObject

import scala.concurrent.Future

@Singleton
class CatererService @Inject()(catererDAO: CatererDAO) {

	def create(caterer: Caterer): Future[Unit] = {
		catererDAO.create(caterer)
	}

	def read(id: String): Future[List[JsObject]] = catererDAO.read(id)

	def update(caterer: Caterer): Future[Unit] = {
		catererDAO.update(caterer)
	}
}
