package jsonb

import play.api.libs.json._


/**
  * Created by jflight on 9/3/2016.
  */
case class SingleVerse(book: Book, chapter: Int, verse: Int)
  extends VerseLocation



object SingleVerseParser extends JsonParserBase[SingleVerse] {


  override def toJsObject(singleVerse: SingleVerse): JsObject = Json.obj(
    "book" -> singleVerse.book.name,
    "chapter" -> singleVerse.chapter,
    "verse" -> singleVerse.verse)


  override def fromJson(jsObject: JsObject): SingleVerse =
    SingleVerse(Books.fromName((jsObject \ "book").as[String]),
      (jsObject \ "chapter").as[Int],
      (jsObject \ "verse").as[Int])

}



