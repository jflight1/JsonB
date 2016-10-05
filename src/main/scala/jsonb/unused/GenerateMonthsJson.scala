package jsonb.unused

import java.io.PrintWriter

import jsonb.{DayReading, DayReadingParser}

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
      val json: String = DayReadingParser.seqToJson(dayReadings)

      val outFileName = "C:\\jflight\\software\\JsonB\\src\\main\\resources\\months\\json\\" + sMonthNum + ".json"

      val printWriter: PrintWriter = new PrintWriter(outFileName)
      printWriter.println(json)
      printWriter.close()
      println(i)
    })

    print("done")
  }


  def main(args: Array[String]) {

    processAll()

  }
}