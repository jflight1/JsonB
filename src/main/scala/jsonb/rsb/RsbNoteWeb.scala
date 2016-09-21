package jsonb.rsb

import java.io.PrintWriter
import java.util.Date

import jsonb._
import play.api.libs.json._

import scala.io.Source



/**
  * This class represents the json for a Reformation Study Bible note we get from the web,
  * i.e. an url like this: https://www.biblegateway.com/exbib/?pub=reformation-study-bible&chunk=187307
  */
case class RsbNoteWeb(verseLocations: Seq[VerseLocation],
                      id: Long,
                      title: String,
                      html: String)



object RsbNoteWebJsonParser extends JsonParserBase[RsbNoteWeb] {


  override def toJsObject(rsbNoteWeb: RsbNoteWeb): JsObject = Json.obj(
    "id" -> rsbNoteWeb.id,
    "title" -> rsbNoteWeb.title,
    "html" -> rsbNoteWeb.html)


  override def fromJson(jsObject: JsObject): RsbNoteWeb = RsbNoteWeb(
    Nil,
    (jsObject \ "id").as[Long],
    (jsObject \ "title").as[String],
    (jsObject \ "html").as[String])



}

