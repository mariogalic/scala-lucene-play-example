package controllers

import javax.inject._
import play.api._
import play.api.libs.json.Json
import play.api.mvc._
import repo.ConditionsRepository

@Singleton class ConditionsController @Inject()(
    cc: ControllerComponents,
    repo: ConditionsRepository
) extends AbstractController(cc) {

  def exact(term: String) = Action { implicit request: Request[AnyContent] =>
    Ok(Json.toJson(repo.searchByExactTitle(term)))
  }

  def fuzzy(term: String) = Action { implicit request: Request[AnyContent] =>
    Ok(Json.toJson(repo.searchByFuzzyTitle(term)))
  }

  def phrase(term: String) = Action { implicit request: Request[AnyContent] =>
    Ok(Json.toJson(repo.searchByPhraseTitle(term)))
  }

  def wildcard(term: String) = Action { implicit request: Request[AnyContent] =>
    Ok(Json.toJson(repo.searchByWildcardTitle(term)))
  }

  def fulltext(term: String) = Action { implicit request: Request[AnyContent] =>
    Ok(Json.toJson(repo.searchByFullText(term)))
  }
}
