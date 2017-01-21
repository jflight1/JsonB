package jsonb

import jsonb.Assert._
import org.junit.Assert._
import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner


@RunWith(classOf[JUnitRunner])
class DayReadingTest extends FunSuite {


  test("parseMonthTextFile") {
    val dayReadings: List[DayReading] = MonthTextFileParser.parseMonthTextFile("/day_reading/txt/01.txt", 1)
    validateJanuaryDayReading(dayReadings)
  }


  test("parseMonthJsonFile") {
    val dayReadings: Seq[DayReading] = DayReadingParser.parseMonthJsonFile(1)
    validateJanuaryDayReading(dayReadings)
  }


  private def validateJanuaryDayReading(dayReadings: Seq[DayReading]) {
    val Genesis = Books.find("genesis")
    val Exodus = Books.find("exodus")
    val Matthew = Books.find("matthew")
    val Psalms = Books.find("psalms")
    val Proverbs = Books.find("proverbs")

    assertEquals(31, dayReadings.size)

/* jlf fix
    // genesis+1:1-2:25;matthew+1:1-2:12;psalm+1:1-1:6;proverbs+1:1-1:6
    assertDayReadingsEqual(
      DayReading(1, 1,
        VerseRange(SingleVerse(Genesis, 1, 1), SingleVerse(Genesis, 2, 25)),
        VerseRange(SingleVerse(Matthew, 1, 1), SingleVerse(Matthew, 2, 12)),
        VerseRange(SingleVerse(Psalms, 1, 1), SingleVerse(Psalms, 1, 6)),
        VerseRange(SingleVerse(Proverbs, 1, 1), SingleVerse(Proverbs, 1, 6))),
      dayReadings(0))

    // genesis+50:1-26;exodus+1:1-2:10;matthew+16:13-17:9;psalm+21:1-13;proverbs+5:1-6
    assertDayReadingsEqual(
      DayReading(1, 25,
        VerseRange(SingleVerse(Genesis, 50, 1), SingleVerse(Exodus, 2, 10)),
        VerseRange(SingleVerse(Matthew, 16, 13), SingleVerse(Matthew, 17, 9)),
        VerseRange(SingleVerse(Psalms, 21, 1), SingleVerse(Psalms, 21, 13)),
        VerseRange(SingleVerse(Proverbs, 5, 1), SingleVerse(Proverbs, 5, 6))),
      dayReadings(24))

    // exodus+12:14-13:16;matthew+20:29-21:22;psalm+25:16-22;proverbs+6:12-15
    assertDayReadingsEqual(
      DayReading(1, 31,
        VerseRange(SingleVerse(Exodus, 12, 14), SingleVerse(Exodus, 13, 16)),
        VerseRange(SingleVerse(Matthew, 20, 29), SingleVerse(Matthew, 21, 22)),
        VerseRange(SingleVerse(Psalms, 25, 16), SingleVerse(Psalms, 25, 22)),
        VerseRange(SingleVerse(Proverbs, 6, 12), SingleVerse(Proverbs, 6, 15))),
      dayReadings(30))
*/
  }

}


