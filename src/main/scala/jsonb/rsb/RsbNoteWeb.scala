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
  extends ToJson {

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


}



object RsbNoteWebJsonParser extends JsonParser[RsbNoteWeb] {



  override def parse(jsObject: JsObject): RsbNoteWeb = new RsbNoteWeb(jsObject)

  override def parse(json: String): RsbNoteWeb = {
    val jsObject: JsObject = Json.parse(json).as[JsObject]
    new RsbNoteWeb(jsObject)
  }



}

