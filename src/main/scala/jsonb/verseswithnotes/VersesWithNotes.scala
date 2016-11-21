package jsonb.verseswithnotes

import jsonb._
import jsonb.niv.NivVerse
import jsonb.rsb.{RsbNoteJsonParser, RsbNote}
import play.api.libs.json.{Json, JsValue}


/**
  * Verses and associated notes
  */
case class VersesWithNotes(singleVerses: Seq[SingleVerse], rsbNotes: Seq[RsbNote])


object VersesWithNotesParser extends JsonParserBase[VersesWithNotes] {

  override def toJsValue(versesWithNotes: VersesWithNotes): JsValue = {
    val singleVersesJsArray = SingleVerseParser.seqToJsArray(versesWithNotes.singleVerses)
    val rsbNotesJsArray = RsbNoteJsonParser.seqToJsArray(versesWithNotes.rsbNotes)

    Json.obj(
      "singleVerses" -> singleVersesJsArray,
      "rsbNotes" -> rsbNotesJsArray)
  }


  override def fromJson(jsValue: JsValue): VersesWithNotes = throw new UnsupportedOperationException
}


object GenerateVersesWithNotesFiles {

  /**
    * Write files like: resources/verses_with_notes/11/20_old.json
    */
  def main(args: Array[String]): Unit = {
//    (1 to 12)
    (3 to 12)
      .foreach(iMonth => {
        val fileNameStart = "verses_with_notes\\" + Utils.paddedString(iMonth) + "\\"
        val dayReadings: Seq[DayReading] = DayReadingParser.parseMonthJsonFile(iMonth)

        dayReadings.foreach(dayReading => {

          def writeDayFile(verseRange: VerseRange, fileNameSuffix: String): Unit = {
            val versesWithNotes: Seq[VersesWithNotes] = verseRange.versesWithNotes
            val fileName: String = fileNameStart + Utils.paddedString(dayReading.day) + "_" + fileNameSuffix + ".json"

            try {
              println("verseRange: " + verseRange + ", month: " + dayReading.month + ", day: " + dayReading.day)
              VersesWithNotesParser.writeSeqToFile(versesWithNotes, fileName)
            }
            catch {
              case e: Exception => {
                println("verseRange: " + verseRange + ", month: " + dayReading.month + ", day: " + dayReading.day)
                e.printStackTrace()
              }
            }
          }

          writeDayFile(dayReading.oldTestament, "old")
          writeDayFile(dayReading.newTestament, "new")
          writeDayFile(dayReading.psalms, "psalms")
          writeDayFile(dayReading.proverbs, "proverbs")
        })
      })

  }

}


