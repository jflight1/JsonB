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


  override def fromJson(jsValue: JsValue): SingleVerse =
    SingleVerse(Books.find((jsValue \ "book").as[String]),
      (jsValue \ "chapter").as[Int],
      (jsValue \ "verse").as[Int])

}



