package jsonb

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date


/**
  * dayReadingSectionsForYear contains the schedule for reading Psalms and Proverbs in a year.
  */
object PsalmsProverbsYear {


  private lazy val yearDayReadings: Seq[DayReading] = {
    def appendDayReadings(monthNum: Int): Seq[DayReading] = {
      if (monthNum > 12)
        Seq()
      else
        DayReadingParser.parseMonthJsonFile(monthNum) ++ appendDayReadings(monthNum + 1)
    }

    appendDayReadings(1)
  }

  private lazy val psalmsVerseRanges: Seq[VerseRange] = {
    yearDayReadings
      .map(dayReading => dayReading.psalms.verseRange)
      .slice(183,365)
  }

  private lazy val proverbsVersRanges: Seq[VerseRange] = {
    val proverbsDayReadingSections = yearDayReadings.map(dayReading => dayReading.proverbs)

    val verseRanges: Seq[VerseRange] = Range(0, 182)
      .map(index => {
        val verseRange1 = proverbsDayReadingSections(2*index).verseRange
        val verseRange2 = proverbsDayReadingSections(2*index + 1).verseRange
        VerseRange(verseRange1.start, verseRange2.end)
      })

    verseRanges :+ proverbsDayReadingSections(364).verseRange
  }


  lazy val dayReadingSectionsForYear: Seq[DayReadingSection] =
    (psalmsVerseRanges ++ proverbsVersRanges)
      .map(verseRange => verseRange.dayReadingSection)


  /**
    * for testing
    */
  def main(args: Array[String]): Unit = {
    val dateFormat = new SimpleDateFormat("MMM dd")

    def intToDate(i: Int): Date = {
      val c = Calendar.getInstance()
      c.set(Calendar.YEAR, 2013)
      c.set(Calendar.MONTH, Calendar.JANUARY)
      c.set(Calendar.DATE, 1)
      c.set(Calendar.HOUR, 0)
      c.set(Calendar.MINUTE, 0)
      c.set(Calendar.SECOND, 0)
      c.set(Calendar.MILLISECOND, 0)
      c.add(Calendar.DATE, i-1)
      c.getTime
    }

    dayReadingSectionsForYear.indices.foreach(i => {

      val date = dateFormat.format(intToDate(i + 1))
      println(date + "\t" + dayReadingSectionsForYear(i).displayString)
    })
  }
}

