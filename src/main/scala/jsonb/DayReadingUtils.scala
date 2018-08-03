package jsonb

import java.io.{InputStream, PrintWriter}

import jsonb.html.{Attribute, Element, SimpleHtmlFileWriter, StringHtmlObject}
import PsalmsProverbsYear
import org.apache.commons.io.IOUtils
import play.api.libs.json.{JsArray, Json}

import scala.io.BufferedSource



object MonthTextFileParser  {


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
        dayReadingSection(verseRanges.head), //(0),
        dayReadingSection(verseRanges(1)),
        dayReadingSection(verseRanges(2)),
        dayReadingSection(verseRanges(3)))

    }
    catch {
      case e: Exception => throw new Exception("Error parsing: " + line, e)
    }
  }

  private def dayReadingSection(verseRange: VerseRange): DayReadingSection = {
    DayReadingSection(verseRange, verseRange.bibleGatewayUrl, verseRange.displayString)
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


object V5FileGenerator {
  def main(args: Array[String]): Unit = {
    generateMonthJsonFiles(1, 0)
  }

  def generateMonthJsonFiles(month: Int,
                             psalmsProverbsDayReadingSectionsIndex: Int): Unit = {
    if (month > 12) return

    // get v4 DayReadings for the month
    val sMonthNum = if (month < 10) "0" + month else "" + month
    val inFileName = "/day_reading/json_v4/" + sMonthNum + ".json"
    val inputStream: InputStream = getClass.getResourceAsStream(inFileName)
    val json: String = IOUtils.toString(inputStream, "UTF-8")
    val jsArray: JsArray = Json.parse(json).as[JsArray]
    val v4DayReadings: Seq[DayReading] = jsArray.value.map(jsValue => DayReadingParser.fromJson(jsValue))

    // map V4 DayReadings to V5
    val v5DayReadings = v4DayReadings.map(v4DayReading => {

      val psalmsProverbsDayReadingSection = PsalmsProverbsYear.dayReadingSectionsForYear(
        psalmsProverbsDayReadingSectionsIndex + v4DayReading.day - 1)

      DayReadingV5(
        v4DayReading.month,
        v4DayReading.day,
        v4DayReading.oldTestament,
        v4DayReading.newTestament,
        v4DayReading.psalms,
        v4DayReading.proverbs,
        psalmsProverbsDayReadingSection)
    })

    val v5Json: String = DayReadingParserV5.seqToJson(v5DayReadings)

    val fileName = "src\\main\\resources\\day_reading\\json_v5\\" + sMonthNum + ".json"
    val printWriter: PrintWriter = new PrintWriter(fileName)
    printWriter.println(v5Json)
    printWriter.close()

    // do the next month
    generateMonthJsonFiles(month + 1,
      psalmsProverbsDayReadingSectionsIndex + v5DayReadings.size)
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
          if (oldTestament) td(link(dayReading.oldTestament.verseRange)) else null,
          if (newTestament) td(link(dayReading.newTestament.verseRange)) else null,
          if (psalms) td(link(dayReading.psalms.verseRange)) else null,
          if (proverbs) td(link(dayReading.proverbs.verseRange)) else null
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

