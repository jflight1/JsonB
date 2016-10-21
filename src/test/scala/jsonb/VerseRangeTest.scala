package jsonb

import jsonb.Assert._
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
        "  \"start\" : \"genesis,1,2\", " +
        "  \"end\" : \"exodus,3,4\" " +
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



  test("parseMonthTextFileLine") {
    val verseRanges: List[VerseRange] = VerseRangeParser
      .parseMonthTextFileLine("genesis+39:1-41:16;matthew+12:46-13:23;psalm+17:1-15")

    assertVerseRangesEqual(VerseRange(
      SingleVerse(TestBooks.Genesis, 39, 1), SingleVerse(TestBooks.Genesis, 41, 16)),
      verseRanges(0))

    assertVerseRangesEqual(VerseRange(
      SingleVerse(TestBooks.Matthew, 12, 46), SingleVerse(TestBooks.Matthew, 13, 23)),
      verseRanges(1))

    assertVerseRangesEqual(VerseRange(
      SingleVerse(TestBooks.Psalms, 17, 1), SingleVerse(TestBooks.Psalms, 17, 15)),
      verseRanges(2))


  }



}


