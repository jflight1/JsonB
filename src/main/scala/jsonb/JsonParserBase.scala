package jsonb

import java.io.{PrintWriter, InputStream}

import play.api.libs.json.{JsArray, JsObject, JsValue, Json}


abstract class JsonParserBase[T] {

  def toJsValue(t: T): JsValue

  def fromJson(jsValue: JsValue): T

  def toJson(t: T): String = Json.prettyPrint(toJsValue(t))


  def fromJson(json: String): T = {
    val jsObject: JsObject = Json.parse(json).as[JsObject]
    fromJson(jsObject)
  }


  def seqToJson(seq: Seq[T]): String = {
    val jsValues: Seq[JsValue] = seq.map(t => toJsValue(t))
    val jsValue: JsValue = Json.toJson(jsValues)
    Json.prettyPrint(jsValue)
  }


  def seqToJsArray(seq: Seq[T]): JsArray = {
    val jsValues: Seq[JsValue] = seq.map(t => toJsValue(t))
    Json.toJson(jsValues).as[JsArray]
  }


  /**
    * @param fileName Assumed to be in resources
    */
  def readSeqFromFile(fileName: String): Seq[T] = {
    val inputStream: InputStream = getClass.getResourceAsStream(fileName)
    val jsArray: JsArray = Json.parse(inputStream).as[JsArray]
    jsArray.value.map(jsValue => fromJson(jsValue.as[JsObject]))
  }


  /**
    * @param fileName Relative to resources
    */
  def writeSeqToFile(seq: Seq[T], fileName: String): Unit = {
    val json: String = seqToJson(seq)
    val printWriter: PrintWriter = new PrintWriter("src\\main\\resources\\" + fileName)
    printWriter.println(json)
    printWriter.close()
  }
}
