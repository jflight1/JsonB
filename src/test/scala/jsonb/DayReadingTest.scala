package jsonb

import jsonb.Assert._
import org.junit.Assert._
import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner


@RunWith(classOf[JUnitRunner])
class DayReadingTest extends FunSuite {

  object TestBooks {
    val Genesis = Books.find("genesis")
    val Exodus = Books.find("exodus")
    val Matthew = Books.find("matthew")
    val Psalms = Books.find("psalms")
    val Proverbs = Books.find("proverbs")
  }


  test("parseMonthTextFile") {
    val dayReadings: List[DayReading] = DayReadingParser.parseMonthTextFile("/months/txt/01.txt", 1)

    assertEquals(31, dayReadings.size)

    // genesis+1:1-2:25;matthew+1:1-2:12;psalm+1:1-1:6;proverbs+1:1-1:6
    assertDayReadingsEqual(
      DayReading(1, 1,
        VerseRange(SingleVerse(TestBooks.Genesis, 1, 1), SingleVerse(TestBooks.Genesis, 2, 25)),
        VerseRange(SingleVerse(TestBooks.Matthew, 1, 1), SingleVerse(TestBooks.Matthew, 2, 12)),
        VerseRange(SingleVerse(TestBooks.Psalms, 1, 1), SingleVerse(TestBooks.Psalms, 1, 6)),
        VerseRange(SingleVerse(TestBooks.Proverbs, 1, 1), SingleVerse(TestBooks.Proverbs, 1, 6))),
      dayReadings(0))

    // genesis+50:1-26;exodus+1:1-2:10;matthew+16:13-17:9;psalm+21:1-13;proverbs+5:1-6
    assertDayReadingsEqual(
      DayReading(1, 25,
        VerseRange(SingleVerse(TestBooks.Genesis, 50, 1), SingleVerse(TestBooks.Exodus, 2, 10)),
        VerseRange(SingleVerse(TestBooks.Matthew, 16, 13), SingleVerse(TestBooks.Matthew, 17, 9)),
        VerseRange(SingleVerse(TestBooks.Psalms, 21, 1), SingleVerse(TestBooks.Psalms, 21, 13)),
        VerseRange(SingleVerse(TestBooks.Proverbs, 5, 1), SingleVerse(TestBooks.Proverbs, 5, 6))),
      dayReadings(24))

    // exodus+12:14-13:16;matthew+20:29-21:22;psalm+25:16-22;proverbs+6:12-15
    assertDayReadingsEqual(
      DayReading(1, 31,
        VerseRange(SingleVerse(TestBooks.Exodus, 12, 14), SingleVerse(TestBooks.Exodus, 13, 16)),
        VerseRange(SingleVerse(TestBooks.Matthew, 20, 29), SingleVerse(TestBooks.Matthew, 21, 22)),
        VerseRange(SingleVerse(TestBooks.Psalms, 25, 16), SingleVerse(TestBooks.Psalms, 25, 22)),
        VerseRange(SingleVerse(TestBooks.Proverbs, 6, 12), SingleVerse(TestBooks.Proverbs, 6, 15))),
      dayReadings(30))


  }



}


