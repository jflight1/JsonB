package jsonb.verseswithnotes

import jsonb._
import jsonb.niv.NivVerse
import jsonb.rsb.RsbNote
import play.api.libs.json.JsValue


/**
  * Verses and associated notes
  */
case class VersesWithNotes(singleVerses: Seq[SingleVerse], rsbNotes: Seq[RsbNote])


object VersesWithNotesParser extends JsonParserBase[VersesWithNotes] {
  override def toJsValue(t: VersesWithNotes): JsValue = ???

  override def fromJson(jsValue: JsValue): VersesWithNotes = ???
}


object GenerateVersesWithNotesFiles {

  def run(): Unit = {
    (1 to 12)
      .foreach(iMonth => {

      val dayReadings: Seq[DayReading] = DayReadingParser.parseMonthJsonFile(iMonth)


        dayReadings.foreach(dayReading => {

          def writeDayFile(verseRange: VerseRange, fileNameSuffix: String): Unit = {

            val fileName: String = Utils.paddedString(dayReading.day) + "_" + fileNameSuffix + ".json"
            val versesWithNotes: Seq[VersesWithNotes] = verseRange.versesWithNotes

//            VersesWithNotesParser.
          }

          writeDayFile(dayReading.oldTestament, "old")
          writeDayFile(dayReading.newTestament, "new")
          writeDayFile(dayReading.psalms, "psalms")
          writeDayFile(dayReading.proverbs, "proverbs")
        })


    })

  }

}


