package jsonb

import java.io.{InputStream, PrintWriter}

import jsonb.html.{Attribute, Element, SimpleHtmlFileWriter, StringHtmlObject}
import org.apache.commons.io.IOUtils
import play.api.libs.json.{JsArray, JsObject, JsValue, Json}

import scala.io.BufferedSource


case class DayReading(month: Int,
                      day: Int,
                      oldTestament: VerseRange,
                      newTestament: VerseRange,
                      psalms: VerseRange,
                      proverbs: VerseRange
                     )


case class DayReadingSection(verseRange: VerseRange,
                             url: String,
                             displayString: String)

object DayReadingSectionParser extends JsonParserBase[DayReadingSection] {
  override def fromJson(jsValue: JsValue): DayReadingSection = throw new UnsupportedOperationException

  override def toJsValue(dayReadingSection: DayReadingSection): JsValue = Json.obj(
    "verseRange" -> VerseRangeParser.toJsValue(dayReadingSection.verseRange),
    "url" -> dayReadingSection.url,
    "displayString" -> dayReadingSection.displayString)
}


case class DayReadingV4(month: Int,
                        day: Int,
                        oldTestament: DayReadingSection,
                        newTestament: DayReadingSection,
                        psalms: DayReadingSection,
                        proverbs: DayReadingSection
                       )


object DayReadingParser extends JsonParserBase[DayReading] {
  val MONTH: String = "month"
  val DAY: String = "day"
  val OLD_TESTAMENT: String = "oldTestament"
  val NEW_TESTAMENT: String = "newTestament"
  val PSALMS: String = "psalms"
  val PROVERBS: String = "proverbs"


  /**
    * generate v4 json
    */
  override def toJsValue(dayReading: DayReading): JsObject = {
    val dayReadingV4 = DayReadingV4(
      dayReading.month,
      dayReading.day,
      dayReadingSection(dayReading.oldTestament),
      dayReadingSection(dayReading.newTestament),
      dayReadingSection(dayReading.psalms),
      dayReadingSection(dayReading.proverbs)
    )

    Json.obj(
      MONTH -> dayReading.month,
      DAY -> dayReading.day,
      OLD_TESTAMENT -> DayReadingSectionParser.toJsValue(dayReadingV4.oldTestament),
      NEW_TESTAMENT -> DayReadingSectionParser.toJsValue(dayReadingV4.newTestament),
      PSALMS -> DayReadingSectionParser.toJsValue(dayReadingV4.psalms),
      PROVERBS -> DayReadingSectionParser.toJsValue(dayReadingV4.proverbs))
  }

  private def dayReadingSection(verseRange: VerseRange): DayReadingSection = {
    DayReadingSection(verseRange, verseRange.bibleGatewayUrl, verseRange.displayString)
  }



  override def fromJson(jsValue: JsValue): DayReading = DayReading(
    JsonHelper.getInt(jsValue, MONTH),
    JsonHelper.getInt(jsValue, DAY),
    VerseRangeParser.fromJson(JsonHelper.getJsValue(jsValue, OLD_TESTAMENT)),
    VerseRangeParser.fromJson(JsonHelper.getJsValue(jsValue, NEW_TESTAMENT)),
    VerseRangeParser.fromJson(JsonHelper.getJsValue(jsValue, PSALMS)),
    VerseRangeParser.fromJson(JsonHelper.getJsValue(jsValue, PROVERBS)))


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
        verseRanges.head, //(0),
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


  def parseMonthJsonFile(month: Int): Seq[DayReading] = {
    val sMonthNum = if (month < 10) "0" + month else "" + month
    val inFileName = "/day_reading/json/" + sMonthNum + ".json"
    val inputStream: InputStream = getClass.getResourceAsStream(inFileName)
    val json: String = IOUtils.toString(inputStream, "UTF-8")
    val jsArray: JsArray = Json.parse(json).as[JsArray]
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
    val inFileName = "/day_reading/json_v1/" + sMonthNum + ".json"
    val inputStream: InputStream = getClass.getResourceAsStream(inFileName)
    val json: String = IOUtils.toString(inputStream, "UTF-8")
    val jsArray: JsArray = Json.parse(json).as[JsArray].head.as[JsArray]
    val dayReadings: Seq[DayReading] = jsArray.value.map(jsValue => DayReadingParser.fromJson(jsValue))
    val v2Json: String = DayReadingParser.seqToJson(dayReadings)

    val fileName = "src\\main\\resources\\day_reading\\json2\\" + sMonthNum + ".json"
    val printWriter: PrintWriter = new PrintWriter(fileName)
    printWriter.println(v2Json)
    printWriter.close()
  }

  def main(args: Array[String]): Unit = {
    //generateAll()

    print("aaaaaaaaaaaa")
  }
}


/**
  * Generates the version 2 month json files
  */
object V3FileGenerator {


  def main(args: Array[String]): Unit = {
    (1 to 12).foreach(month => {
      generateMonthJsonFile(month)
    })
  }

  def generateMonthJsonFile(month: Int): Unit = {
    val sMonthNum = if (month < 10) "0" + month else "" + month
    val inFileName = "/day_reading/json/" + sMonthNum + ".json"
    val inputStream: InputStream = getClass.getResourceAsStream(inFileName)
    val json: String = IOUtils.toString(inputStream, "UTF-8")
    val jsArray: JsArray = Json.parse(json).as[JsArray]
    val dayReadings: Seq[DayReading] = jsArray.value.map(jsValue => DayReadingParser.fromJson(jsValue))
    val v3Json: String = DayReadingParser.seqToJson(dayReadings)

    val fileName = "src\\main\\resources\\day_reading\\json_v3\\" + sMonthNum + ".json"
    val printWriter: PrintWriter = new PrintWriter(fileName)
    printWriter.println(v3Json)
    printWriter.close()
  }
}



object V4FileGenerator {


  def main(args: Array[String]): Unit = {
    (1 to 12).foreach(month => {
      generateMonthJsonFile(month)
    })
  }

  def generateMonthJsonFile(month: Int): Unit = {
    val sMonthNum = if (month < 10) "0" + month else "" + month
    val inFileName = "/day_reading/json_v3/" + sMonthNum + ".json"
    val inputStream: InputStream = getClass.getResourceAsStream(inFileName)
    val json: String = IOUtils.toString(inputStream, "UTF-8")
    val jsArray: JsArray = Json.parse(json).as[JsArray]
    val dayReadings: Seq[DayReading] = jsArray.value.map(jsValue => DayReadingParser.fromJson(jsValue))
    val v4Json: String = DayReadingParser.seqToJson(dayReadings)

    val fileName = "src\\main\\resources\\day_reading\\json_v4\\" + sMonthNum + ".json"
    val printWriter: PrintWriter = new PrintWriter(fileName)
    printWriter.println(v4Json)
    printWriter.close()
  }
}


/**
  * Generate html files with day reading links
  */
object TableGeneratorObject {

  /**
    * The generated html uses a table
    */
  private class TableGenerator(month: Int,
                               subFolder: String,
                               oldTestament: Boolean,
                               newTestament: Boolean,
                               psalms: Boolean,
                               proverbs: Boolean){

    def writeFile(): Unit = {
      val dayReadings = DayReadingParser.parseMonthJsonFile(month)

      val rows: Seq[Element] = dayReadings.map(dayReading => {

        val tds: Seq[Element] = Seq(
          td("" + month + "/" + dayReading.day),
          if (oldTestament) td(link(dayReading.oldTestament)) else null,
          if (newTestament) td(link(dayReading.newTestament)) else null,
          if (psalms) td(link(dayReading.psalms)) else null,
          if (proverbs) td(link(dayReading.proverbs)) else null
        )
          .filter(_ != null)

        Element("tr", Nil, tds)
      })

      val table = Element("table", Nil, rows)

      val outFileName = "day_reading\\" + subFolder + "\\" +
        Utils.paddedTwoDigitInt(month) + ".htm"

      SimpleHtmlFileWriter(outFileName, Seq(table)).write()
    }


    private def td(text: String): Element = Element("td", Nil, Seq(StringHtmlObject(text)))

    private def td(element: Element): Element = Element("td", Nil, Seq(element))

    private def link(verseRange: VerseRange): Element = {
      Element("a",
        Seq(Attribute("target", "_blank"), Attribute("href", verseRange.bibleGatewayUrl)),
        Seq(StringHtmlObject(verseRange.displayString)))
    }
  }


  def main(args: Array[String]): Unit = {
    (1 to 12).foreach(month => {
      new TableGenerator(month, "new_proverbs", false, true, false, true).writeFile()
      println("done " + month)
    })
  }
}

