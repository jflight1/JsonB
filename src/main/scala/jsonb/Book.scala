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
case class Book(oneYearBibleName: String, exbibName: String, nivName: String, isOldTestament: Boolean, chapterNumVerses: Seq[Int]) {

  def nameMatches(name: String): Boolean = {
    oneYearBibleName.toLowerCase == cleanName(name) ||
      exbibName.toLowerCase == cleanName(name) ||
      (oneYearBibleName == "psalms" && cleanName(name).contains("psalm")) // special case because you say "psalm 23"
  }

  def cleanName(name: String) = name.trim.toLowerCase

  def numChapters: Int = chapterNumVerses.size

}


object BookParser extends JsonParserBase[Book] {

  override def toJsObject(book: Book): JsObject = Json.obj(
    "oneYearBibleName" -> book.oneYearBibleName,
    "exbibName" -> book.exbibName,
    "nivName" -> book.nivName,
    "isOldTestament" -> book.isOldTestament,
    "chapterNumVerses" -> Json.toJson(book.chapterNumVerses))


  override def fromJson(jsObject: JsObject): Book =
    Book(
      (jsObject \ "oneYearBibleName").as[String],
      (jsObject \ "exbibName").as[String],
      (jsObject \ "nivName").as[String],
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


