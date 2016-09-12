package jsonb

import java.io.{StringWriter, InputStream}

import scala.io.BufferedSource

/**
  * Reads the month txt files and generates the month json files
  */
object GenerateMonthsJson {

  private def processAll(): Unit = {
    (1 to 12).foreach(i => {
      println(i)
      val sMonthNum = if (i < 10) "0" + i else "" + i
      val inFileName = "/months/txt/" + sMonthNum + ".txt"
      val dayReadings: List[DayReading] = DayReadingParser.parseMonthFile(inFileName, i)
      val json: String = JsonHelper.listToJson(dayReadings)

//      val stringWriter: StringWriter = new StringWriter(json)
      print(json)
    })

  }


  def main(args: Array[String]) {

    processAll()

  }
}
