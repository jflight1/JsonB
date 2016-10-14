package jsonb

import play.api.libs.json.JsValue

/**
  * Anything that is json
  */
trait JsonParser[T] {


  def toJson(t: T): String

  def toJsValue(t: T): JsValue

  def seqToJson(list: Seq[T]): String

  def fromJson(json: String): T

  def fromJson(jsValue: JsValue): T

  def readSeqFromFile(fileName: String): Seq[T]

}
