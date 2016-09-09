package jsonb

import play.api.libs.json.JsObject

/**
  * Anything that is json
  */
trait JsonParser[T] {

  def parse(json: String): T

  def parse(jsObject: JsObject): T

}
