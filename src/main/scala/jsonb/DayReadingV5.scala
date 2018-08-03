package jsonb

import java.io.InputStream

import org.apache.commons.io.IOUtils
import play.api.libs.json.{JsArray, JsObject, JsValue, Json}



case class DayReadingV5(month: Int,
                        day: Int,
                        oldTestament: DayReadingSection,
                        newTestament: DayReadingSection,
                        psalms: DayReadingSection,
                        proverbs: DayReadingSection,
                        psalmsAndProverbs: DayReadingSection
                       )


object DayReadingParserV5 extends JsonParserBase[DayReadingV5] {
  val MONTH: String = "month"
  val DAY: String = "day"
  val OLD_TESTAMENT: String = "oldTestament"
  val NEW_TESTAMENT: String = "newTestament"
  val PSALMS: String = "psalms"
  val PROVERBS: String = "proverbs"
  val PSALMS_AND_PROVERBS: String = "psalmsAndProverbs"


  /**
    * generate v4 json
    */
  override def toJsValue(dayReading: DayReadingV5): JsObject = {

    Json.obj(
      MONTH -> dayReading.month,
      DAY -> dayReading.day,
      OLD_TESTAMENT -> DayReadingSectionParser.toJsValue(dayReading.oldTestament),
      NEW_TESTAMENT -> DayReadingSectionParser.toJsValue(dayReading.newTestament),
      PSALMS -> DayReadingSectionParser.toJsValue(dayReading.psalms),
      PROVERBS -> DayReadingSectionParser.toJsValue(dayReading.proverbs),
      PSALMS_AND_PROVERBS -> DayReadingSectionParser.toJsValue(dayReading.psalmsAndProverbs))
  }




  override def fromJson(jsValue: JsValue): DayReadingV5 = DayReadingV5(
    JsonHelper.getInt(jsValue, MONTH),
    JsonHelper.getInt(jsValue, DAY),
    DayReadingSectionParser.fromJson(JsonHelper.getJsValue(jsValue, OLD_TESTAMENT)),
    DayReadingSectionParser.fromJson(JsonHelper.getJsValue(jsValue, NEW_TESTAMENT)),
    DayReadingSectionParser.fromJson(JsonHelper.getJsValue(jsValue, PSALMS)),
    DayReadingSectionParser.fromJson(JsonHelper.getJsValue(jsValue, PROVERBS)),
    DayReadingSectionParser.fromJson(JsonHelper.getJsValue(jsValue, PSALMS_AND_PROVERBS)))


  /**
    * Returns DayReadings for the given month number (January = 1, etc.)
    */
  def parseMonthJsonFile(month: Int): Seq[DayReadingV5] = {
    val sMonthNum = if (month < 10) "0" + month else "" + month
    val inFileName = "/day_reading/json_v4/" + sMonthNum + ".json"
    val inputStream: InputStream = getClass.getResourceAsStream(inFileName)
    val json: String = IOUtils.toString(inputStream, "UTF-8")
    val jsArray: JsArray = Json.parse(json).as[JsArray]
    jsArray.value.map(jsValue => DayReadingParserV5.fromJson(jsValue))
  }
}


case class DayReadingSection(verseRange: VerseRange,
                             url: String,
                             displayString: String)

object DayReadingSectionParser extends JsonParserBase[DayReadingSection] {
  override def fromJson(jsValue: JsValue): DayReadingSection = DayReadingSection(
    VerseRangeParser.fromJson(JsonHelper.getJsValue(jsValue, "verseRange")),
    JsonHelper.getString(jsValue, "url"),
    JsonHelper.getString(jsValue, "displayString")
  )

  override def toJsValue(dayReadingSection: DayReadingSection): JsValue = Json.obj(
    "verseRange" -> VerseRangeParser.toJsValue(dayReadingSection.verseRange),
    "url" -> dayReadingSection.url,
    "displayString" -> dayReadingSection.displayString)
}
