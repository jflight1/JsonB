package jsonb

import java.io.{InputStream, PrintWriter}

import play.api.libs.json._

import scala.io.BufferedSource



/**
  * @param index Canonical index number; starts at 1.
  * @param oneYearBibleName Name in oneyearbibleonline.com.  http://oneyearbibleonline.com/...
  *                         Also this is the name in the daily reading files: \resources\months\txt\*.txt
  * @param exbibName Name used in the exbib api.  https://www.biblegateway.com/exbib/contents/?osis=...
  * @param nivName Name in NIV.json
  * @param chapterNumVerses The number of verses in each chapter
  */
case class Book(index: Int, oneYearBibleName: String, exbibName: String, nivName: String, rsbNoteName: String,
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

  override def toJsValue(book: Book): JsObject = Json.obj(
    "index" -> book.index,
    "oneYearBibleName" -> book.oneYearBibleName,
    "exbibName" -> book.exbibName,
    "nivName" -> book.nivName,
    "rsbNoteName" -> book.rsbNoteName,
    "isOldTestament" -> book.isOldTestament,
    "chapterNumVerses" -> Json.toJson(book.chapterNumVerses))


  override def fromJson(jsValue: JsValue): Book =
    Book(
      (jsValue \ "index").as[Int],
      (jsValue \ "oneYearBibleName").as[String],
      (jsValue \ "exbibName").as[String],
      (jsValue \ "nivName").as[String],
      (jsValue \ "rsbNoteName").as[String],
      (jsValue \ "isOldTestament").as[Boolean],
      (jsValue \ "chapterNumVerses").as[Seq[Int]])
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

  def allBooksSortedLargestToSmallest: Seq[Book] = {
    Books.allBooks
      .sortBy(b => -b.chapterNumVerses.sum)
  }


  def main(args: Array[String]): Unit = {
    allBooksSortedLargestToSmallest.foreach(b => println(b.oneYearBibleName + "\t" + b.chapterNumVerses.sum))
  }
}


