package jsonb

import org.junit.Assert._
import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner


@RunWith(classOf[JUnitRunner])
class VerseRangeTest extends FunSuite {

  object TestBooks {
    val Genesis = Books.find("genesis")
    val Exodus = Books.find("exodus")
    val Matthew = Books.find("matthew")
    val Psalms = Books.find("psalms")
    val Proverbs = Books.find("proverbs")
  }


  test("toJson") {

    val jsonString =VerseRangeParser.toJson(VerseRange(
      SingleVerse(TestBooks.Genesis, 1, 2), SingleVerse(TestBooks.Exodus, 3, 4)))

    val expected = "{\r\n  \"start\" : \"genesis,1,2\",\r\n  \"end\" : \"exodus,3,4\"\r\n}"
    assert(jsonString == expected)
  }


  test("parse") {
    val jsonString: String =
      "{ " +
        "  \"start\" : { " +
        "    \"book\" : \"genesis\", " +
        "    \"chapter\" : 1, " +
        "    \"verse\" : 2 " +
        "  }, " +
        "  \"end\" : { " +
        "    \"book\" : \"exodus\", " +
        "    \"chapter\" : 3, " +
        "    \"verse\" : 4 " +
        "  } " +
        "} "


    val verseRange: VerseRange = VerseRangeParser.fromJson(jsonString)

    val expected: VerseRange = VerseRange(
      SingleVerse(TestBooks.Genesis, 1, 2), SingleVerse(TestBooks.Exodus, 3, 4))
    assertVerseRangesEqual(expected, verseRange)
  }



  test("parseText one chapter") {
    val verseRange: VerseRange = VerseRangeParser.parseText("matthew+11:7-30")

    val expected: VerseRange = VerseRange(
      SingleVerse(TestBooks.Matthew, 11, 7), SingleVerse(TestBooks.Matthew, 11, 30))
    assertVerseRangesEqual(expected, verseRange)
  }


  test("parseText 2 chapters") {
    val verseRange: VerseRange = VerseRangeParser.parseText("genesis+16:1-18:15")

    val expected: VerseRange = VerseRange(
      SingleVerse(TestBooks.Genesis, 16, 1), SingleVerse(TestBooks.Genesis, 18, 15))
    assertVerseRangesEqual(expected, verseRange)
  }


  test("parseText one verse") {
    val verseRange: VerseRange = VerseRangeParser.parseText("proverbs+10:5")
    val expected: VerseRange = VerseRange(
      SingleVerse(TestBooks.Proverbs, 10, 5), SingleVerse(TestBooks.Proverbs, 10, 5))
    assertVerseRangesEqual(expected, verseRange)

  }



  test("parseMonthFileLine") {
    val verseRanges: List[VerseRange] = VerseRangeParser
      .parseMonthFileLine("genesis+39:1-41:16;matthew+12:46-13:23;psalm+17:1-15")

    assertVerseRangesEqual(VerseRange(
      SingleVerse(TestBooks.Genesis, 39, 1), SingleVerse(TestBooks.Genesis, 39, 41)),
      verseRanges(0))

    assertVerseRangesEqual(VerseRange(
      SingleVerse(TestBooks.Matthew, 12, 46), SingleVerse(TestBooks.Matthew, 12, 13)),
      verseRanges(1))

    assertVerseRangesEqual(VerseRange(
      SingleVerse(TestBooks.Psalms, 17, 1), SingleVerse(TestBooks.Psalms, 17, 15)),
      verseRanges(2))


  }


  /**
    * Parse a line like this:
    *   genesis+39:1-41:16;matthew+12:46-13:23;psalm+17:1-15;proverbs+3:33-35
    */
  /*
    def parseMonthFileLine(line: String): List[VerseRange] = {
      // each part is either a VerseRange or SingleVerse.
      // VerseRange is just two SingleVerses, so we can turn the parts into a
      // List[SingleVerse]
      val parts: Array[String] = line.split(";")
      linePartsToVerseRanges(parts)
    }
  */

  private def assertVerseRangesEqual(vr1: VerseRange, vr2: VerseRange): Unit = {
  }


}


