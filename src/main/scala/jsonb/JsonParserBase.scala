package jsonb

import play.api.libs.json.Json.JsValueWrapper
import play.api.libs.json.{JsValue, JsArray, Json, JsObject}



/**
  * Created by jflight on 9/21/2016.
  */
abstract class JsonParserBase[T] extends JsonParser[T] {

  override def toJson(t: T): String = Json.prettyPrint(toJsObject(t))


  override def fromJson(json: String): T = {
    val jsObject: JsObject = Json.parse(json).as[JsObject]
    fromJson(jsObject)
  }


  override def seqToJson(seq: Seq[T]): String = {

    val jsObjects: Seq[JsObject] = seq.map(t => toJsObject(t))
    val jsValue: JsValue = Json.toJson(jsObjects)
    Json.prettyPrint(jsValue)
  }
}
