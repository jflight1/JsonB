package jsonb.rsb

import jsonb.{Book, VerseRangeParser, JsonParserBase}
import play.api.libs.json.{JsValue, Json, JsObject}



object RsbNoteJsonParser extends JsonParserBase[RsbNote] {

  override def toJsValue(rsbNote: RsbNote): JsObject = Json.obj(
    "verseRange" -> VerseRangeParser.toJsValue(rsbNote.verseRange),
    "id" -> rsbNote.id,
    "title" -> rsbNote.title,
    "text" -> rsbNote.text)


  override def fromJson(jsValue: JsValue): RsbNote = {
    RsbNote(
      VerseRangeParser.fromJson((jsValue \ "verseRange").as[JsObject]),
      (jsValue \ "id").as[String].toLong,
      (jsValue \ "title").as[String],
      (jsValue \ "text").as[String])
  }


  def generateJsonFile(book: Book) = ???


  /**
    * Get the json for all the notes in a book
    */
  def json(book: Book): String = ???

}
