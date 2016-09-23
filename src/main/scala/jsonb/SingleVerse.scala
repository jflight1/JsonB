package jsonb

import play.api.libs.json._


/**
  * Created by jflight on 9/3/2016.
  */
case class SingleVerse(book: BookInfo, chapter: Int, verse: Int)
  extends VerseLocation



object SingleVerseParser extends JsonParserBase[SingleVerse] {


  override def toJsObject(singleVerse: SingleVerse): JsObject = Json.obj(
    "book" -> singleVerse.book.oneYearBibleName,
    "chapter" -> singleVerse.chapter,
    "verse" -> singleVerse.verse)


  override def fromJson(jsObject: JsObject): SingleVerse =
    SingleVerse(BookInfos.find((jsObject \ "book").as[String]),
      (jsObject \ "chapter").as[Int],
      (jsObject \ "verse").as[Int])

}



