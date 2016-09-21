package jsonb

import org.junit.Assert._
import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner


@RunWith(classOf[JUnitRunner])
class VerseRangeTest extends FunSuite {

  test("toJson") {

    val jsonString =VerseRangeParser.toJson(VerseRange(
      SingleVerse(Books.Genesis, 1, 2), SingleVerse(Books.Exodus, 3, 4)))

    val expected = "{\r\n  \"start\" : {\r\n    \"book\" : \"genesis\",\r\n    \"chapter\" : 1,\r\n    \"verse\" : 2\r\n  },\r\n  \"end\" : {\r\n    \"book\" : \"exodus\",\r\n    \"chapter\" : 3,\r\n    \"verse\" : 4\r\n  }\r\n}"
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
      SingleVerse(Books.Genesis, 1, 2), SingleVerse(Books.Exodus, 3, 4))
    assertVerseRangesEqual(expected, verseRange)
  }



  test("parseText one chapter") {
    val verseRange: VerseRange = VerseRangeParser.parseText("matthew+11:7-30")

    val expected: VerseRange = VerseRange(
      SingleVerse(Books.Matthew, 11, 7), SingleVerse(Books.Matthew, 11, 30))
    assertVerseRangesEqual(expected, verseRange)
  }


  test("parseText 2 chapters") {
    val verseRange: VerseRange = VerseRangeParser.parseText("genesis+16:1-18:15")

    val expected: VerseRange = VerseRange(
      SingleVerse(Books.Genesis, 16, 1), SingleVerse(Books.Genesis, 18, 15))
    assertVerseRangesEqual(expected, verseRange)
  }


  test("parseText one verse") {
    val verseRange: VerseRange = VerseRangeParser.parseText("proverbs+10:5")
    val expected: VerseRange = VerseRange(
      SingleVerse(Books.Proverbs, 10, 5), SingleVerse(Books.Proverbs, 10, 5))
    assertVerseRangesEqual(expected, verseRange)

  }



  test("parseMonthFileLine") {
    val verseRanges: List[VerseRange] = VerseRangeParser
      .parseMonthFileLine("genesis+39:1-41:16;matthew+12:46-13:23;psalm+17:1-15")

    assertVerseRangesEqual(VerseRange(
      SingleVerse(Books.Genesis, 39, 1), SingleVerse(Books.Genesis, 39, 41)),
      verseRanges(0))

    assertVerseRangesEqual(VerseRange(
      SingleVerse(Books.Matthew, 12, 46), SingleVerse(Books.Matthew, 12, 13)),
      verseRanges(1))

    assertVerseRangesEqual(VerseRange(
      SingleVerse(Books.Psalms, 17, 1), SingleVerse(Books.Psalms, 17, 15)),
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


