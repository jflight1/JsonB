package jsonb

import play.api.libs.json.{JsArray, Json, JsObject}



/**
  * Created by jflight on 9/21/2016.
  */
abstract class JsonParserBase[T] extends JsonParser[T] {

  override def toJson(t: T): String = Json.prettyPrint(toJsObject(t))


  override def fromJson(json: String): T = {
    val jsObject: JsObject = Json.parse(json).as[JsObject]
    fromJson(jsObject)
  }


  override def listToJson(list: List[T]): String = {
    val jsObjects: List[JsObject] = list.map(t => toJsObject(t))
    val jsArray: JsArray = Json.arr(jsObjects)
    Json.prettyPrint(jsArray)
  }
}
