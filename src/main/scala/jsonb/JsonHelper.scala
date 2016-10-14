package jsonb

import play.api.libs.json.{JsObject, JsValue}


/**
  * Created by jflight on 9/3/2016.
  */
object JsonHelper {

  def getInt(jsValue: JsValue, field: String) =
    (jsValue \ field).as[Int]

  def getString(jsValue: JsValue, field: String) =
    (jsValue \ field).as[String]

  def getJsObject(jsValue: JsValue, field: String): JsObject =
    (jsValue \ field).as[JsObject]

  def getJsValue(jsValue: JsValue, field: String): JsValue =
    (jsValue \ field).as[JsValue]


}

