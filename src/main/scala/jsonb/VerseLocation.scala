package jsonb

import play.api.libs.json._


/**
  * Created by jflight on 9/3/2016.
  */
case class VerseLocation(book: Book, chapter: Int, verse: Int) extends ToJson {

  def this(jsObject: JsObject) = {
    this(Books.fromName((jsObject \ "book").as[String]),
      (jsObject \ "chapter").as[Int],
      (jsObject \ "verse").as[Int])
  }


  override def toJsObject: JsObject = Json.obj(
    "book" -> book.name,
    "chapter" -> chapter,
    "verse" -> verse)


  override def toJson: String = Json.prettyPrint(toJsObject)
}



object VerseLocationParser extends JsonParser[VerseLocation] {


  override def parse(jsObject: JsObject): VerseLocation = new VerseLocation(jsObject)

  override def parse(json: String): VerseLocation = {
    val jsObject: JsObject = Json.parse(json).as[JsObject]
    new VerseLocation(jsObject)
  }

  def parseText(text: String): Option[VerseLocation] = ???



}



