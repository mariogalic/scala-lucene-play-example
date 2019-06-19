package repo

import java.io.{FileNotFoundException, IOException}
import com.fasterxml.jackson.core.JsonParseException
import javax.inject.{Inject, Singleton}
import play.api.Environment
import play.api.libs.json.{JsResultException, Json}
import scala.util.{Failure, Success, Try}

case class Condition(title: String, text: String)
object Condition { implicit val format = Json.format[Condition] }

@Singleton class FileImporter @Inject()(environment: Environment) {
  def asConditions(file: String = "data.json"): List[Condition] =
    Try {
      environment
        .resourceAsStream(file)
        .map(Json.parse)
        .map(_.as[List[Condition]])
        .map(_.distinct)
    } match { /* Fail fast by design on problems with data */
      case Success(Some(conditions: List[Condition])) => conditions
      case Success(None) => throw new FileNotFound(file)
      case Failure(e: JsonParseException) => throw new InvalidJsonSyntax(file, e)
      case Failure(e: JsResultException) => throw new InvalidJsonSchema(file, e)
      case Failure(e) => throw new IOException(s"Failed to read $file", e)
    }
}

class FileNotFound(file: String) extends FileNotFoundException(s"$file not found")
class InvalidJsonSyntax(file: String, cause: Throwable) extends IOException(s"Invalid JSON syntax in $file", cause)
class InvalidJsonSchema(file: String, cause: Throwable) extends IOException(s"""Invalid JSON schema in $file. Expected schema: [{"title": "string", "description": "string"},...]""", cause)
