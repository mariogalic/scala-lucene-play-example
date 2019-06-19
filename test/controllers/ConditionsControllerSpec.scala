package controllers

import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.test._
import play.api.test.Helpers._
import repo.Condition

class ConditionsControllerSpec extends PlaySpec with GuiceOneAppPerTest with Injecting {
  "ConditionsController" should {
    "search by exact title" in {
      val request = FakeRequest(GET, "/conditions/exact?term=Arthritis")
      val home = route(app, request).get

      status(home) mustBe OK
      contentType(home) mustBe Some("application/json")
      contentAsString(home) must include ("Arthritis")
      contentAsJson(home).as[List[Condition]].size must be (3)
    }

    "search by fuzzy title" in {
      val request = FakeRequest(GET, "/conditions/fuzzy?term=Ahritis")
      val home = route(app, request).get

      status(home) mustBe OK
      contentType(home) mustBe Some("application/json")
      contentAsString(home) must include ("Arthritis")
      contentAsJson(home).as[List[Condition]].size must be (3)
    }

    "search by phrase title" in {
      val request = FakeRequest(GET, "/conditions/phrase?term=Rheumatoid Treatment")
      val home = route(app, request).get

      status(home) mustBe OK
      contentType(home) mustBe Some("application/json")
      contentAsString(home) must include ("Rheumatoid")
      contentAsString(home) must include ("Treatment")
      contentAsJson(home).as[List[Condition]].size must be (1)
    }

    "search by wildcard title" in {
      val request = FakeRequest(GET, "/conditions/wildcard?term=Rheuma*")
      val home = route(app, request).get

      status(home) mustBe OK
      contentType(home) mustBe Some("application/json")
      contentAsString(home) must include ("Rheumatoid")
      contentAsJson(home).as[List[Condition]].size must be (2)
    }

    "search by full text" in {
      val request = FakeRequest(GET, "/conditions/fulltext?term=Arthritis")
      val home = route(app, request).get

      status(home) mustBe OK
      contentType(home) mustBe Some("application/json")
      contentAsString(home) must include ("Arthritis")
      contentAsJson(home).as[List[Condition]].size must be (3)
    }

    "handle no search results" in {
      val request = FakeRequest(GET, "/conditions/exact?term=doesnotexist")
      val home = route(app, request).get

      status(home) mustBe OK
      contentType(home) mustBe Some("application/json")
      contentAsString(home) must include ("[]")
      contentAsJson(home).as[List[Condition]].size must be (0)
    }

    "handle bad request" in {
      val request = FakeRequest(GET, "/conditions/exact?badparam=Arthritis")
      val home = route(app, request).get

      status(home) mustBe BAD_REQUEST
    }
  }
}
