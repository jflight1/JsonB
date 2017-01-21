package jsonb.verseswithnotes

import jsonb._
import jsonb.niv.NivVerse
import jsonb.rsb.{RsbNoteJsonParser, RsbNote}
import play.api.libs.json.{JsArray, JsObject, Json, JsValue}


/**
  * Verses and associated notes
  */
case class VersesWithNotes(singleVerses: Seq[SingleVerse], rsbNotes: Seq[RsbNote])


/**
  * VersesWithNotes Json Parser
  */
object VersesWithNotesParser extends JsonParserBase[VersesWithNotes] {

  override def toJsValue(versesWithNotes: VersesWithNotes): JsValue = {
    val verseJsObjects: Seq[JsObject] = versesWithNotes.singleVerses
      .map(singleVerse => Json.obj(
        "verse" -> singleVerse.toString,
        "text" -> singleVerse.nivVerse.text))

    val rsbNotesJsArray = RsbNoteJsonParser.seqToJsArray(versesWithNotes.rsbNotes)

    Json.obj(
      "verses" -> Json.toJson(verseJsObjects).as[JsArray],
      "rsbNotes" -> rsbNotesJsArray)
  }


  override def fromJson(jsValue: JsValue): VersesWithNotes = throw new UnsupportedOperationException
}


object GenerateVersesWithNotesJsonFiles {

  /**
    * Write files like: resources/verses_with_notes/11/20_old.json
    */
  def main(args: Array[String]): Unit = {
    (1 to 12)
      .foreach(iMonth => {
        val fileNameStart = "verses_with_notes\\" + Utils.paddedTwoDigitInt(iMonth) + "\\"
        val dayReadings: Seq[DayReading] = DayReadingParser.parseMonthJsonFile(iMonth)

        dayReadings.foreach(dayReading => {

          def writeDayFile(verseRange: VerseRange, fileNameSuffix: String): Unit = {
            val versesWithNotes: Seq[VersesWithNotes] = verseRange.versesWithNotes
            val fileName: String = fileNameStart + Utils.paddedTwoDigitInt(dayReading.day) + "_" + fileNameSuffix + ".json"

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

          writeDayFile(dayReading.oldTestament.verseRange, "old")
          writeDayFile(dayReading.newTestament.verseRange, "new")
          writeDayFile(dayReading.psalms.verseRange, "psalms")
          writeDayFile(dayReading.proverbs.verseRange, "proverbs")
        })
      })

  }
}



object GenerateVersesWithNotesHtmlFiles {

  /**
    * Write files like: resources/verses_with_notes/11/20_old.json
    */
  def main(args: Array[String]): Unit = {
    (1 to 1)
      .foreach(iMonth => {
        val sMonth = Utils.paddedTwoDigitInt(iMonth)
        val fileName = "/verses_with_notes/" + sMonth + "/" + sMonth + "_new.json"

        val versesWithNotesSeq: Seq[VersesWithNotes] = VersesWithNotesParser.readSeqFromFile(fileName)

        versesWithNotesSeq.foreach(versesWithNotes => println(versesWithNotes))


      })
  }
}