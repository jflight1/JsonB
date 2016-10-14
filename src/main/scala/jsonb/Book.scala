package jsonb

import java.io.{InputStream, PrintWriter}

import play.api.libs.json._

import scala.io.BufferedSource



/**
  *
  * @param oneYearBibleName Name in oneyearbibleonline.com.  http://oneyearbibleonline.com/...
  *                         Also this is the name in the daily reading files: \resources\months\txt\*.txt
  * @param exbibName Name used in the exbib api.  https://www.biblegateway.com/exbib/contents/?osis=...
  * @param nivName Name in NIV.json
  * @param chapterNumVerses The number of verses in each chapter
  */
case class Book(oneYearBibleName: String, exbibName: String, nivName: String, rsbNoteName: String,
                isOldTestament: Boolean, chapterNumVerses: Seq[Int]) {


  /**
    * name can match any of the names
    */
  def nameMatches(name: String): Boolean = {

    def nameMatches(name: String, okNames: Seq[String]): Boolean = {
      okNames.map(okName => cleanName(okName)) match {
        case Nil => false;
        case `name` :: theRest => true
        case okName :: theRest => nameMatches(name, theRest)
      }
    }

    nameMatches(cleanName(name), Seq(oneYearBibleName, exbibName, nivName, rsbNoteName)) ||
      (cleanName(name) == "psalm" && oneYearBibleName == "psalms") // need special case
  }

  private def cleanName(name: String) = name.trim.toLowerCase

  def numChapters: Int = chapterNumVerses.size

}


object BookParser extends JsonParserBase[Book] {

  override def toJsObject(book: Book): JsObject = Json.obj(
    "oneYearBibleName" -> book.oneYearBibleName,
    "exbibName" -> book.exbibName,
    "nivName" -> book.nivName,
    "rsbNoteName" -> book.rsbNoteName,
    "isOldTestament" -> book.isOldTestament,
    "chapterNumVerses" -> Json.toJson(book.chapterNumVerses))


  override def fromJson(jsObject: JsObject): Book =
    Book(
      (jsObject \ "oneYearBibleName").as[String],
      (jsObject \ "exbibName").as[String],
      (jsObject \ "nivName").as[String],
      (jsObject \ "rsbNoteName").as[String],
      (jsObject \ "isOldTestament").as[Boolean],
      (jsObject \ "chapterNumVerses").as[Seq[Int]])
}


object Books {

  lazy val allBooks: Seq[Book] = BookParser.readSeqFromFile("/books.json")

  def find(name: String): Book = {
    val books: Seq[Book] = allBooks.filter(book => book.nameMatches(name))

    if (books == Nil || books.size > 1) {
      throw new Exception("Bad Book name: " + name)
    }

    books.head
  }
}


