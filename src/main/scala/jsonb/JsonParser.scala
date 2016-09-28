package jsonb

import play.api.libs.json.JsObject

/**
  * Anything that is json
  */
trait JsonParser[T] {


  def toJson(t: T): String

  def toJsObject(t: T): JsObject

  def seqToJson(list: Seq[T]): String

  def fromJson(json: String): T

  def fromJson(jsObject: JsObject): T

}
