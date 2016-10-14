package jsonb.niv

import java.io.{PrintWriter, InputStream}

import jsonb.{Book, Books, JsonParserBase}
import play.api.libs.json.{JsValue, JsArray, JsObject, Json}

import scala.collection.Set



/**
  * Represents a book in NIV.json
  * Right now all we have is the book name but the plan is to add more
  */
case class NivBook(name: String)


case class BookWithChapterNumVerses(book: Book, chapterNumVerses: Seq[Int])


object BookWithChapterNumVersesParser extends JsonParserBase[BookWithChapterNumVerses] {

  override def toJsValue(bookWithChapterNumVerses: BookWithChapterNumVerses): JsObject = {
    val book: Book = bookWithChapterNumVerses.book

    val arr: JsArray = Json.arr(bookWithChapterNumVerses.chapterNumVerses)
    Json.obj(
      "oneYearBibleName" -> book.oneYearBibleName,
      "exbibName" -> book.exbibName,
      "nivName" -> book.nivName,
      "isOldTestament" -> book.isOldTestament,
      "chapterNumVerses" -> Json.toJson(bookWithChapterNumVerses.chapterNumVerses)
    )
  }

  override def fromJson(jsValue: JsValue): BookWithChapterNumVerses = {
    throw new UnsupportedOperationException
  }
}


object NivBookUtils {

  lazy val allBooks: Seq[NivBook] = {
    val inputStream: InputStream = getClass.getResourceAsStream("/NIV.json")
    val rootJsObject: JsObject = Json.parse(inputStream).as[JsObject]
    val keys: Set[String] = rootJsObject.keys
    keys.map(key => NivBook(key)).toSeq
  }

  /**
    * Map NIV book names to Seq which is the number of verses in each chapter
    */
  def bookChapterNumVerses: Map[String, Seq[Int]] = {
    val inputStream: InputStream = getClass.getResourceAsStream("/NIV.json")
    val rootJsObject: JsObject = Json.parse(inputStream).as[JsObject]
    val bookNames: Seq[String] = rootJsObject.keys.toList
    val initialMap: Map[String, Seq[Int]] = Map()
    bookChapterNumVerses(bookNames, rootJsObject, initialMap)
  }

  def bookChapterNumVerses(bookNames: Seq[String], rootJsObject: JsObject, mapSoFar: Map[String, Seq[Int]])
  : Map[String, Seq[Int]] = {

    bookNames match {
      case Nil => mapSoFar

      case bookName :: theRest =>
        val numVersesSeq: Seq[Int] = numVerses(bookName, rootJsObject)
        val nextMap: Map[String, Seq[Int]] = mapSoFar + (bookName -> numVersesSeq)
        bookChapterNumVerses(theRest, rootJsObject, nextMap)
    }
  }

  def numVerses(bookName: String, rootJsObject: JsObject): Seq[Int] = {
    numVerses((rootJsObject \ bookName).as[JsObject])
  }

  def numVerses(bookJsObject: JsObject): Seq[Int] = {
    val numChapters = bookJsObject.keys.size

    (1 to numChapters)
      .map(chapterNum => {
        val versesJsObject: JsObject = (bookJsObject \ chapterNum.toString).as[JsObject]
        versesJsObject.keys.size
      })
  }



  def writeBooksToFile() = {

    val map: Map[String, Seq[Int]] = bookChapterNumVerses

    val booksWithChapterNumVerses: Seq[BookWithChapterNumVerses] = Books.allBooks
      .map(book => {
        val chapterNumVerses: Seq[Int] = map.get(book.nivName).get
        BookWithChapterNumVerses(book, chapterNumVerses)
      })

    val json: String = BookWithChapterNumVersesParser.seqToJson(booksWithChapterNumVerses)
    val fileName = "src\\main\\resources\\books_v3.json"
    val printWriter: PrintWriter = new PrintWriter(fileName)
    printWriter.println(json)
    printWriter.close()
  }


  def main(args: Array[String]): Unit = {

    writeBooksToFile()


  }

}