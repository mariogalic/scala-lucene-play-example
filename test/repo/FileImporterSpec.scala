package repo

import org.scalatestplus.play._
import play.api.Environment

class FileImporterSpec extends PlaySpec {
  val fileImporter = new FileImporter(Environment.simple())

  "FileImporter" should {
    "deserilise into Seq[Condition]" in {
      val expectedModel = Seq(
        Condition(text = "Some description of arthitis", title = "Arthritis "),
        Condition(text = "Some description of neutropenia", title = "Neutropenia"),
        Condition(
          text = "Some description of Rheumatoid arthritis - Treatment",
          title = "Rheumatoid arthritis - Treatment".trim
        ),
        Condition(
          text = "Some description of Rheumatoid arthritis - Symptoms",
          title = "Rheumatoid arthritis - Symptoms".trim
        )
      )
      fileImporter.asConditions() must be(expectedModel)
    }

    "deduplicate" in {
      fileImporter.asConditions("duplicates.json").size must be(2)
    }

    "throw exception on invalid JSON syntax" in {
      intercept[InvalidJsonSyntax] {
        fileImporter.asConditions("invalid.json")
      }
    }

    "throw exception on file not found" in {
      intercept[FileNotFound] {
        fileImporter.asConditions("notfound.json")
      }
    }

    "throw exception on invalid JSON schema" in {
      intercept[InvalidJsonSchema] {
        fileImporter.asConditions("bad-schema.json")
      }
    }
  }
}
