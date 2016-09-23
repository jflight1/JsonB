package jsonb.rsb

import jsonb._
import play.api.libs.json._


/**
  * This class represents the json for a Reformation Study Bible note.  Our json
  * representation is different from what we get from the web.  We represent those
  * with RsbNoteWeb.
  */
case class RsbNote(verseRange: VerseRange,
                   id: Long,
                   title: String,
                   html: String)


/*
  def this(jsObject: JsObject) = {
    this(
      Nil,
      (jsObject \ "id").as[Long],
      (jsObject \ "title").as[String],
      (jsObject \ "html").as[String])

//    (jsObject \ "verseLocations").as[JsArray].value.map(jsObject => )
  }


  override def toJsObject: JsObject = Json.obj(
    "id" -> id,
    "title" -> title,
    "html" -> html)


  override def toJson: String = Json.prettyPrint(toJsObject)
*/
