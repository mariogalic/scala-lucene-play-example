package repo

import org.scalatestplus.play._
import play.api.Environment

class ConditionsRepositorySpec extends PlaySpec {
  val fileImporter = new FileImporter(Environment.simple())
  val repo = new ConditionsRepository(fileImporter)
  val expectedArthritis = Condition(text = "Some description of arthitis", title = "Arthritis ".trim)
  val expectedRheuma = Condition(
    text = "Some description of Rheumatoid arthritis - Treatment",
    title = "Rheumatoid arthritis - Treatment".trim
  )

  "ConditionsRepository" should {
    "stores conditions with trimmed titles (no surrounding whitespace)" in {
      repo.searchByExactTitle("Arthritis").head.title must be("Arthritis ".trim)
    }

    "search by exact title" in {
      repo.searchByExactTitle("Arthritis") must contain(expectedArthritis)
      repo.searchByExactTitle("Arthritis").size must be(3)
    }

    "search by fuzzy title" in {
      repo.searchByFuzzyTitle("Ahritis") must contain(expectedArthritis)
    }

    "search by phrase title" in {
      repo.searchByPhraseTitle("Rheumatoid Treatment") must contain(expectedRheuma)
      repo.searchByPhraseTitle("Rheumatoid Treatment").size must be(1)
    }

    "search by wildcard title" in {
      repo.searchByWildcardTitle("Rheuma*").size must be(2)
      repo.searchByWildcardTitle("Rheuma*") must contain(expectedRheuma)
    }

    "search by full text" in {
      repo.searchByFullText("arthritis").size must be(3)
      repo.searchByFullText("arthritis") must contain(expectedArthritis)
    }
  }
}
