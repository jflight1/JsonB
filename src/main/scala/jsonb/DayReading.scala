package jsonb

import play.api.libs.json.{JsObject, Json}


/**
  * Created by jflight on 9/3/2016.
  */

private object Keys {
  val MONTH: String = "month"
  val DAY: String = "day"
  val OLD_TESTAMENT: String = "oldTestament"
  val NEW_TESTAMENT: String = "newTestament"
  val PSALMS: String = "psalms"
  val PROVERBS: String = "proverbs"
}


case class DayReading(month: Int,
                      day: Int,
                      oldTestament: VerseRange,
                      newTestament: VerseRange,
                      psalms: VerseRange,
                      proverbs: VerseRange
                     ) extends ToJson {



  def this(jsObject: JsObject) = {
    this(
      JsonHelper.getInt(jsObject, Keys.MONTH),
      JsonHelper.getInt(jsObject, Keys.DAY),
      VerseRangeParser.parse(JsonHelper.getJsObject(jsObject, Keys.OLD_TESTAMENT)),
      VerseRangeParser.parse(JsonHelper.getJsObject(jsObject, Keys.NEW_TESTAMENT)),
      VerseRangeParser.parse(JsonHelper.getJsObject(jsObject, Keys.PSALMS)),
      VerseRangeParser.parse(JsonHelper.getJsObject(jsObject, Keys.PROVERBS)))
  }

  override def toJsObject: JsObject = Json.obj(
    Keys.MONTH -> month,
    Keys.DAY -> day,
    Keys.OLD_TESTAMENT -> oldTestament.toJsObject,
    Keys.NEW_TESTAMENT -> newTestament.toJsObject,
    Keys.PSALMS -> psalms.toJsObject,
    Keys.PROVERBS -> proverbs.toJsObject)

  override def toJson: String = Json.prettyPrint(toJsObject)

}



object DayReadingParser extends JsonParser[DayReading] {

  override def parse(jsObject: JsObject): DayReading = new DayReading(jsObject)

  override def parse(json: String): DayReading = {
    val jsObject: JsObject = Json.parse(json).as[JsObject]
    new DayReading(jsObject)
  }


  def parseMonthFile(fileName: String, month: Int): List[DayReading] = ???


  /**
    * Parse a line like this:
    *   genesis+39:1-41:16;matthew+12:46-13:23;psalm+17:1-15;proverbs+3:33-35
    */
  def parseMonthFileLine(line: String, month: Int): DayReading = ???



}



