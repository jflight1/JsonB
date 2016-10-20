package jsonb

import java.io.{PrintWriter, InputStream}

import jsonb.V2FileGenerator._
import org.apache.commons.io.IOUtils
import play.api.libs.json.{JsArray, JsObject, JsValue, Json}

import scala.io.BufferedSource


/**

  * { // DayReading
  * month: 1,
  * day: 1,
  * old_testament: { // verseRange
  * start: { // SingleVerse
  * book: "genesis",
  * chapter: 1
  * verse: 1
  * }
  * end: {
  * book: "genesis",
  * chapter: 1
  * verse: 28
  * }
  * }
  * new_testament: {
  * ...
  * }
  * psalms: {
  * ...
  * }
  * proverbs: {
  * ...
  * }
  * },


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
                     )


object DayReadingParser extends JsonParserBase[DayReading] {


  override def toJsValue(dayReading: DayReading): JsObject = Json.obj(
    Keys.MONTH -> dayReading.month,
    Keys.DAY -> dayReading.day,
    Keys.OLD_TESTAMENT -> VerseRangeParser.toJsValue(dayReading.oldTestament),
    Keys.NEW_TESTAMENT -> VerseRangeParser.toJsValue(dayReading.newTestament),
    Keys.PSALMS -> VerseRangeParser.toJsValue(dayReading.psalms),
    Keys.PROVERBS -> VerseRangeParser.toJsValue(dayReading.proverbs))


  override def fromJson(jsValue: JsValue): DayReading = DayReading(
    JsonHelper.getInt(jsValue, Keys.MONTH),
    JsonHelper.getInt(jsValue, Keys.DAY),
    VerseRangeParser.fromJson(JsonHelper.getJsValue(jsValue, Keys.OLD_TESTAMENT)),
    VerseRangeParser.fromJson(JsonHelper.getJsValue(jsValue, Keys.NEW_TESTAMENT)),
    VerseRangeParser.fromJson(JsonHelper.getJsValue(jsValue, Keys.PSALMS)),
    VerseRangeParser.fromJson(JsonHelper.getJsValue(jsValue, Keys.PROVERBS)))


  def parseMonthTextFile(fileName: String, month: Int): List[DayReading] = {

    val inputStream: InputStream = getClass.getResourceAsStream(fileName)
    val bufferedSource: BufferedSource = io.Source.fromInputStream(inputStream)
    val lines: Iterator[String] = bufferedSource.getLines()
    parseMonthTextFileLines(lines, month, 1)
  }


  private def parseMonthTextFileLines(lines: Iterator[String], month: Int, startDay: Int): List[DayReading] = {
    if (lines.hasNext) {
      val line: String = lines.next()
      if (line.trim.isEmpty) {
        parseMonthTextFileLines(lines, month, startDay) // skip empty lines
      }
      else {
        val dayReading: DayReading = parseMonthTextFileLine(line, month, startDay)
        dayReading :: parseMonthTextFileLines(lines, month, startDay + 1)
      }
    }

    else Nil
  }


  /**
    * Parse a line like this:
    *   genesis+39:1-41:16;matthew+12:46-13:23;psalm+17:1-15;proverbs+3:33-35
    */
  private def parseMonthTextFileLine(line: String, month: Int, day: Int): DayReading = {

    try {
      val rawVerseRanges = VerseRangeParser.parseMonthTextFileLine(line)
      val verseRanges = combineVerseRanges(rawVerseRanges)
      if (verseRanges.size != 4)
        throw new Exception("Bad line: " + line)

      DayReading(month, day,
        verseRanges(0),
        verseRanges(1),
        verseRanges(2),
        verseRanges(3))

    }
    catch {
      case e: Exception => throw new Exception("Error parsing: " + line, e)
    }
  }


  /**
    * For a line like this
    *   genesis+50:1-26;exodus+1:1-2:10;matthew+16:13-17:9;psalm+21:1-13;proverbs+5:1-6
    * The first two VerseRanges need to get combined into one.
    */
  private def combineVerseRanges(verseRanges: List[VerseRange]): List[VerseRange] = {

    // jlf fix

    verseRanges match {
      // size < 2, do nothing
      case Nil => Nil
      case verseRange :: Nil => verseRanges

      case vr1 :: vr2 :: theRest => {

        def bookType(book: Book): Int = {
          if (book == Books.find("psalms")) 1
          else if (book == Books.find("proverbs")) 2
          else if (book.isOldTestament) 3
          else 4
        }

        // If the book types (i.e. OldTestament) are the same, we combine them
        if (bookType(vr1.start.book) == bookType(vr2.start.book))
          VerseRange(vr1.start, vr2.end) :: combineVerseRanges(theRest)
        else
          vr1 :: vr2 :: combineVerseRanges(theRest)
      }
    }
  }


  def parseMonthJsonFile(month: Int): Seq[DayReading] = {
    val sMonthNum = if (month < 10) "0" + month else "" + month
    val inFileName = "/months/json/" + sMonthNum + ".json"
    val inputStream: InputStream = getClass.getResourceAsStream(inFileName)
    val json: String = IOUtils.toString(inputStream, "UTF-8")
    val jsArray: JsArray = Json.parse(json).as[JsArray].head.as[JsArray]
    jsArray.value.map(jsValue => DayReadingParser.fromJson(jsValue))
  }



}




/**
  * Generates the version 2 month json files
  */
object V2FileGenerator {

  def generateAll(): Unit = {
    (1 to 12).foreach(month => {
      generateMonthJsonFile(month)
    })
  }


  def generateMonthJsonFile(month: Int): Unit = {
    val sMonthNum = if (month < 10) "0" + month else "" + month
    val inFileName = "/months/json/" + sMonthNum + ".json"
    val inputStream: InputStream = getClass.getResourceAsStream(inFileName)
    val json: String = IOUtils.toString(inputStream, "UTF-8")
    val jsArray: JsArray = Json.parse(json).as[JsArray].head.as[JsArray]
    val dayReadings: Seq[DayReading] = jsArray.value.map(jsValue => DayReadingParser.fromJson(jsValue))
    val v2Json: String = DayReadingParser.seqToJson(dayReadings)

    val fileName = "src\\main\\resources\\months\\json2\\" + sMonthNum + ".json"
    val printWriter: PrintWriter = new PrintWriter(fileName)
    printWriter.println(v2Json)
    printWriter.close()
  }



  def main(args: Array[String]): Unit = {
    generateAll()
  }
}