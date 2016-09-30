package jsonb

import java.io.InputStream

import jsonb.BookFactory._
import play.api.libs.iteratee.Enumerator
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


  /**
    * @param fileName Assumed to be in resources
    */
  override def readSeqFromFile(fileName: String): Seq[T] = {
    val inputStream: InputStream = getClass.getResourceAsStream(fileName)
    val jsArray: JsArray = Json.parse(inputStream).as[JsArray]
    jsArray.value.map(jsValue => fromJson(jsValue.as[JsObject]))


  }
}
