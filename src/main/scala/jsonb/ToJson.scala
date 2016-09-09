package jsonb

import play.api.libs.json.JsObject

/**
  * Anything that is json
  */
trait ToJson {


  def toJson: String

  def toJsObject: JsObject

}
