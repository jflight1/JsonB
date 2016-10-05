package jsonb.rsb

import jsonb._
import play.api.libs.json._


/**
  * This class represents the json for a Reformation Study Bible note.  Our json
  * representation is different from what we get from the .  We represent those
  * with RsbNote.
  */
case class RsbNote(verseRange: VerseRange,
                   id: Long,
                   title: String,
                   text: String)


object RsbNoteJsonParser extends JsonParserBase[RsbNote] {

  override def toJsObject(rsbNote: RsbNote): JsObject = Json.obj(
    "verseRange" -> VerseRangeParser.toJsObject(rsbNote.verseRange),
    "id" -> rsbNote.id,
    "title" -> rsbNote.title,
    "text" -> rsbNote.text)

  override def fromJson(jsObject: JsObject): RsbNote = {
    RsbNote(
      VerseRangeParser.fromJson((jsObject \ "verseRange").as[JsObject]),
      (jsObject \ "id").as[String].toLong,
      (jsObject \ "title").as[String],
      (jsObject \ "text").as[String])
  }


  def generateJsonFile(book: Book) = ???


  /**
    * Get the json for all the notes in a book
    */
  def json(book: Book): String = ???



}

