package jsonb.niv

import jsonb.JsonParserBase
import play.api.libs.json.{JsString, JsValue, Json}


/**
  * The json representation of an NivVerse is a string consisting of the verse number,
  * ":" and the text:
  *   "10:The elders of Gilead replied... "
  */
case class NivVerseParser(verseNumber: Int) extends JsonParserBase[NivVerse] {


  override def toJsValue(nivVerse: NivVerse): JsValue =
    JsString(toString(nivVerse))


  def toString(nivVerse: NivVerse): String =
    verseNumber + ":" + nivVerse.text


  /**
    * Note: This differs from fromString below because a json string will actually
    * include quotes as part of the string like: "10:The elders... "
    */
  override def fromJson(json: String): NivVerse = {
    val jsString: JsString = Json.parse(json).as[JsString]
    fromJson(jsString)
  }


  override def fromJson(jsValue: JsValue): NivVerse =
    fromString(jsValue.as[JsString].value)


  def fromString(s: String): NivVerse = {
    val colonIndex: Int = s.indexOf(":")
    val text: String = s.substring(colonIndex + 1)
    NivVerse(text)
  }

}


