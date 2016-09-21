package jsonb

import java.io.InputStream

import play.api.libs.json.{JsObject, Json}

import scala.io.{BufferedSource, Source}


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


  override def toJsObject(dayReading: DayReading): JsObject = Json.obj(
    Keys.MONTH -> dayReading.month,
    Keys.DAY -> dayReading.day,
    Keys.OLD_TESTAMENT -> VerseRangeParser.toJsObject(dayReading.oldTestament),
    Keys.NEW_TESTAMENT -> VerseRangeParser.toJsObject(dayReading.newTestament),
    Keys.PSALMS -> VerseRangeParser.toJsObject(dayReading.psalms),
    Keys.PROVERBS -> VerseRangeParser.toJsObject(dayReading.proverbs))


  override def fromJson(jsObject: JsObject): DayReading = DayReading(
    JsonHelper.getInt(jsObject, Keys.MONTH),
    JsonHelper.getInt(jsObject, Keys.DAY),
    VerseRangeParser.fromJson(JsonHelper.getJsObject(jsObject, Keys.OLD_TESTAMENT)),
    VerseRangeParser.fromJson(JsonHelper.getJsObject(jsObject, Keys.NEW_TESTAMENT)),
    VerseRangeParser.fromJson(JsonHelper.getJsObject(jsObject, Keys.PSALMS)),
    VerseRangeParser.fromJson(JsonHelper.getJsObject(jsObject, Keys.PROVERBS)))


  def parseMonthFile(fileName: String, month: Int): List[DayReading] = {

    val inputStream: InputStream = getClass.getResourceAsStream(fileName)
    val bufferedSource: BufferedSource = io.Source.fromInputStream(inputStream)
    val lines: Iterator[String] = bufferedSource.getLines()
    parseMonthFileLines(lines, month, 1)
  }


  private def parseMonthFileLines(lines: Iterator[String], month: Int, startDay: Int): List[DayReading] = {
    if (lines.hasNext) {
      val line: String = lines.next()
      if (line.trim.isEmpty) {
        parseMonthFileLines(lines, month, startDay) // skip empty lines
      }
      else {
        val dayReading: DayReading = parseMonthFileLine(line, month, startDay)
        dayReading :: parseMonthFileLines(lines, month, startDay + 1)
      }
    }

    else Nil
  }


  /**
    * Parse a line like this:
    *   genesis+39:1-41:16;matthew+12:46-13:23;psalm+17:1-15;proverbs+3:33-35
    */
  private def parseMonthFileLine(line: String, month: Int, day: Int): DayReading = {

    try {
      val rawVerseRanges = VerseRangeParser.parseMonthFileLine(line)
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
    verseRanges match {
      // size < 2, do nothing
      case Nil => Nil
      case verseRange :: Nil => verseRanges

      case vr1 :: vr2 :: theRest =>
        // If the book types (i.e. OldTestament) are the same, we combine them
        if (vr1.start.book.bookType == vr2.start.book.bookType)
          VerseRange(vr1.start, vr2.end) :: combineVerseRanges(theRest)
        else
          vr1 :: vr2 :: combineVerseRanges(theRest)
    }
  }
}



