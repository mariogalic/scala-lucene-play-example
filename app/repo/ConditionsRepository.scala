package repo

import com.outr.lucene4s._
import com.outr.lucene4s.DirectLucene
import com.outr.lucene4s.field.Field
import com.outr.lucene4s.field.value.FieldAndValue
import com.outr.lucene4s.query.{SearchResult, SearchTerm, Sort}
import javax.inject.{Inject, Singleton}

@Singleton class ConditionsRepository @Inject()(fileImporter: FileImporter) {
  private val lucene = new DirectLucene(
    uniqueFields = List("title"),
    defaultFullTextSearchable = true,
    autoCommit = true
  )
  private val title: Field[String] = lucene.create.field[String]("title")
  private val text: Field[String] = lucene.create.field[String]("text")
  initLucene()

  private def toFields(condition: Condition): Seq[FieldAndValue[_]] =
    Seq(title(condition.title.trim), text(condition.text))

  private def addToLucene(condition: Condition): Unit =
    lucene.doc().fields(toFields(condition): _*).index()

  private def initLucene(): Unit =
    fileImporter
      .asConditions()
      .foreach(addToLucene)

  private val exactTitle = title.apply _ andThen exact _
  private val fuzzyTitle = title.apply _ andThen fuzzy _
  private val phraseTitle = parseFuzzy(_ :String, Some(title))
  private val wildcardTitle = title.apply _ andThen wildcard _
  private val fulltext = lucene.fullText.apply _ andThen fuzzy _

  private def toCondition(result: SearchResult): Condition =
    Condition(result(title), result(text))

  private def search(term: String, searchFunc: String => SearchTerm): Vector[Condition] =
    lucene
      .query()
      .scoreDocs()
      .sort(Sort.Score)
      .filter(searchFunc(term.toLowerCase))
      .search()
      .pagedResultsIterator
      .toVector
      .map(toCondition)

  def searchByExactTitle(term: String): Vector[Condition] = search(term, exactTitle)
  def searchByFuzzyTitle(term: String): Vector[Condition] = search(term, fuzzyTitle)
  def searchByPhraseTitle(term: String): Vector[Condition] = search(term, phraseTitle)
  def searchByWildcardTitle(term: String): Vector[Condition] = search(term, wildcardTitle)
  def searchByFullText(term: String): Vector[Condition] = search(term, fulltext)
}
