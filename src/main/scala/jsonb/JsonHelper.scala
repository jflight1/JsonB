package jsonb

import play.api.libs.json.{JsArray, JsObject, Json}


/**
  * Created by jflight on 9/3/2016.
  */
object JsonHelper {

  def getInt(jsObject: JsObject, field: String) =
    (jsObject \ field).as[Int]

  def getString(jsObject: JsObject, field: String) =
    (jsObject \ field).as[String]

  def getJsObject(jsObject: JsObject, field: String) =
    (jsObject \ field).as[JsObject]


}

