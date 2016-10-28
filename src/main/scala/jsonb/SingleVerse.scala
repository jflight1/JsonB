package jsonb

import play.api.libs.json._



case class SingleVerse(book: Book, chapter: Int, verse: Int)
  extends VerseLocation {

  override def toString: String = book.codeName + "," + chapter + "," + verse
}



object SingleVerseParser extends JsonParserBase[SingleVerse] {


  override def toJsValue(singleVerse: SingleVerse): JsValue =
    JsString(singleVerse.toString)


  /**
    * Note: This differs from fromString below because a json string will actually
    * include quotes as part of the string like: "genesis,1,2"
    */
  override def fromJson(json: String): SingleVerse = {
    val jsString: JsString = Json.parse(json).as[JsString]
    fromJson(jsString)
  }


  override def fromJson(jsValue: JsValue): SingleVerse =
    fromString(jsValue.as[JsString].value)


  def fromString(s: String): SingleVerse = {
    val parts: Array[String] = s.split(",")

    SingleVerse(
      book = Books.find(parts(0)),
      chapter = parts(1).toInt,
      verse = parts(2).toInt)
  }

}



