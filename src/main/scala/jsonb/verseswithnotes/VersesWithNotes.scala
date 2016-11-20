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

  override def fromJson(jsValue: JsValue): VersesWithNotes = throw new UnsupportedOperationException
}


object GenerateVersesWithNotesFiles {

  /**
    * Write files like: resources/verses_with_notes/11/20_old.json
    */
  def run(): Unit = {
    (1 to 12)
      .foreach(iMonth => {
        val fileNameStart = "verses_with_notes\\" + Utils.paddedString(iMonth)
        val dayReadings: Seq[DayReading] = DayReadingParser.parseMonthJsonFile(iMonth)

        dayReadings.foreach(dayReading => {

          def writeDayFile(verseRange: VerseRange, fileNameSuffix: String): Unit = {
            val versesWithNotes: Seq[VersesWithNotes] = verseRange.versesWithNotes
            val fileName: String = fileNameStart + Utils.paddedString(dayReading.day) + "_" + fileNameSuffix + ".json"
            VersesWithNotesParser.writeSeqToFile(versesWithNotes, fileName)
          }

          writeDayFile(dayReading.oldTestament, "old")
          writeDayFile(dayReading.newTestament, "new")
          writeDayFile(dayReading.psalms, "psalms")
          writeDayFile(dayReading.proverbs, "proverbs")
        })
      })

  }

}


