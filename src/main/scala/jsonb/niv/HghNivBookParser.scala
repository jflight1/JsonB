package jsonb.niv

import jsonb.{Book, JsonParserBase}
import play.api.libs.json.{JsObject, JsValue}


/**
  * Parse a chapter from hgh_niv.json
  * json representation.  Note chapters aren't in order:
  * {
  *   "11": {
  *     "24": "Will you not take what your god Chemosh ...",
  *     "25": "Are you better than Balak son of Zippor, ...",
  *     "26": "For three hundred years Israel occupied ..."
  *   },
  *   "10": {
  *     ...
  *   },
  *   ...
  * }
  */
case class HghNivBookParser(book: Book) extends JsonParserBase[NivBook] {

  // We never generate HGH json
  override def toJsValue(nivBook: NivBook): JsObject = throw new UnsupportedOperationException


  override def fromJson(jsValue: JsValue): NivBook = {

    val jsObject: JsObject = jsValue.as[JsObject]

    val nivChapters: Seq[NivChapter] = jsObject.keys
      .map(sKey => new IndexAndChapter(jsObject, sKey)) // map key Strings to IndexAndChapters
      .toSeq.sortBy(indexAndVerse => indexAndVerse.index) // sort by index
      .map(indexAndVerse => indexAndVerse.nivChapter) // map to NivChapters

    NivBook(book, nivChapters)
  }

  private class IndexAndChapter(val index: Int, val nivChapter: NivChapter) {
    def this(bookJsObject: JsObject, sKey: String) {
      this(sKey.toInt, nivChapterInBook(bookJsObject, sKey))
    }
  }


  private def nivChapterInBook(bookJsObject: JsObject, sKey: String): NivChapter = {
    val chapterJsObject = (bookJsObject \ sKey).as[JsObject]
    HghNivChapterParser.fromJson(chapterJsObject)
  }

}
