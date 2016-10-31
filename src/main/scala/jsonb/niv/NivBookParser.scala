package jsonb.niv

import jsonb.{Utils, Book, Books, JsonParserBase}
import play.api.libs.json.{JsArray, JsObject, JsValue, Json}

import scala.collection.immutable.IndexedSeq


/**
  * json representation:
  * {
  *   "name": "judges",
  *   "chapters": [
  *     {
  *       "chapterNumber": 1,
  *       "verses": [
  *         ...
  *       ]
  *     },
  *     {
  *       "chapterNumber": 2,
  *       "verses": [
  *         ...
  *       ]
  *     },
  *     ...
  *   ]
  * }
  */
object NivBookParser extends JsonParserBase[NivBook] {


  override def toJsValue(nivBook: NivBook): JsObject = {
    val chaptersJson: IndexedSeq[JsObject] = nivBook.chapters.indices.map(i => NivChapterParser(i + 1).toJsValue(nivBook.chapters(i)))

    Json.obj(
      "name" -> nivBook.book.codeName,
      "chapters" -> chaptersJson)
  }


  override def fromJson(jsValue: JsValue): NivBook = {

    val name: String = (jsValue \ "name").as[String]
    val book: Book = Books.find(name)

    val chapters: Seq[NivChapter] = (jsValue \ "chapters").as[JsArray]
      .value // Seq[JsValue]
      .map(jsValue => NivChapterParser(-1).fromJson(jsValue))

    NivBook(book, chapters)
  }


  def fileName(book: Book): String = {
    val leadingZero = if (book.index < 10) "0" else ""
    leadingZero + book.index + "_" + book.codeName + ".json"
  }


  def fromFile(book: Book): NivBook = {
    val json = Utils.resourceFileToString("/niv/" + fileName(book))
    fromJson(json)
  }
}


