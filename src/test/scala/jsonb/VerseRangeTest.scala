package jsonb

import jsonb.Assert._
import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import org.junit.Assert._


@RunWith(classOf[JUnitRunner])
class VerseRangeTest extends FunSuite {

  val Genesis = Books.find("genesis")
  val Exodus = Books.find("exodus")
  val Matthew = Books.find("matthew")
  val Psalms = Books.find("psalms")
  val Proverbs = Books.find("proverbs")


  test("toJson") {
    assertEquals("\"genesis,1,2-exodus,3,4\"",
      VerseRangeParser.toJson(VerseRange(
        SingleVerse(Genesis, 1, 2), SingleVerse(Exodus, 3, 4))))

    assertEquals("\"genesis,1,2\"",
      VerseRangeParser.toJson(VerseRange(
        SingleVerse(Genesis, 1, 2), SingleVerse(Genesis, 1, 2))))
  }


  test("parse") {
    val jsonString: String =
      "\"genesis,1,2-exodus,3,4\""

    val verseRange: VerseRange = VerseRangeParser.fromJson(jsonString)

    val expected: VerseRange = VerseRange(
      SingleVerse(Genesis, 1, 2), SingleVerse(Exodus, 3, 4))
    assertVerseRangesEqual(expected, verseRange)
  }


  test("parseText one chapter") {
    val verseRange: VerseRange = VerseRangeParser.parseText("matthew+11:7-30")

    val expected: VerseRange = VerseRange(
      SingleVerse(Matthew, 11, 7), SingleVerse(Matthew, 11, 30))
    assertVerseRangesEqual(expected, verseRange)
  }


  test("parseText 2 chapters") {
    val verseRange: VerseRange = VerseRangeParser.parseText("genesis+16:1-18:15")

    val expected: VerseRange = VerseRange(
      SingleVerse(Genesis, 16, 1), SingleVerse(Genesis, 18, 15))
    assertVerseRangesEqual(expected, verseRange)
  }


  test("parseText one verse") {
    val verseRange: VerseRange = VerseRangeParser.parseText("proverbs+10:5")
    val expected: VerseRange = VerseRange(
      SingleVerse(Proverbs, 10, 5), SingleVerse(Proverbs, 10, 5))
    assertVerseRangesEqual(expected, verseRange)

  }



  test("parseMonthTextFileLine") {
    val verseRanges: List[VerseRange] = VerseRangeParser
      .parseMonthTextFileLine("genesis+39:1-41:16;matthew+12:46-13:23;psalm+17:1-15")

    assertVerseRangesEqual(VerseRange(
      SingleVerse(Genesis, 39, 1), SingleVerse(Genesis, 41, 16)),
      verseRanges(0))

    assertVerseRangesEqual(VerseRange(
      SingleVerse(Matthew, 12, 46), SingleVerse(Matthew, 13, 23)),
      verseRanges(1))

    assertVerseRangesEqual(VerseRange(
      SingleVerse(Psalms, 17, 1), SingleVerse(Psalms, 17, 15)),
      verseRanges(2))


  }


  test("singleVerses") {
    val genesis: Book = Books.find("genesis")
    val exodus: Book = Books.find("exodus")

    assertEquals(
      Seq(
        SingleVerse(genesis, 50, 25),
        SingleVerse(genesis, 50, 26),
        SingleVerse(exodus, 1, 1),
        SingleVerse(exodus, 1, 2)
      ),

      VerseRange(SingleVerse(genesis, 50, 25), SingleVerse(exodus, 1, 2))
        .singleVerses)
  }


}


