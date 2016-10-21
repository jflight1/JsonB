package jsonb

import play.api.libs.json._


/**
  * Created by jflight on 9/3/2016.
  */
case class SingleVerse(book: Book, chapter: Int, verse: Int)
  extends VerseLocation



object SingleVerseParser extends JsonParserBase[SingleVerse] {


  override def toJsValue(singleVerse: SingleVerse): JsValue =
    JsString(singleVerse.book.oneYearBibleName + "," + singleVerse.chapter + "," + singleVerse.verse)


  override def fromJson(json: String): SingleVerse = {
    val jsString: JsString = Json.parse(json).as[JsString]
    fromJson(jsString)
  }

  override def fromJson(jsValue: JsValue): SingleVerse = {
    val parts: Array[String] = jsValue.as[JsString].value.split(",")

    SingleVerse(
      book = Books.find(parts(0)),
      chapter = parts(1).toInt,
      verse = parts(2).toInt)
  }

}



