package jsonb

import jsonb.Books._
import jsonb.Assert._
import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import org.junit.Assert._


@RunWith(classOf[JUnitRunner])
class VerseRangeTest extends FunSuite {

  test("toJson") {
    assertEquals("\"genesis,1,2-exodus,3,4\"",
      VerseRangeParser.toJson(VerseRange(
        SingleVerse(genesis, 1, 2), SingleVerse(exodus, 3, 4))))

    assertEquals("\"genesis,1,2\"",
      VerseRangeParser.toJson(VerseRange(
        SingleVerse(genesis, 1, 2), SingleVerse(genesis, 1, 2))))
  }


  test("parse") {
    val jsonString: String =
      "\"genesis,1,2-exodus,3,4\""

    val verseRange: VerseRange = VerseRangeParser.fromJson(jsonString)

    val expected: VerseRange = VerseRange(
      SingleVerse(genesis, 1, 2), SingleVerse(exodus, 3, 4))
    assertVerseRangesEqual(expected, verseRange)
  }


  test("parseText one chapter") {
    val verseRange: VerseRange = VerseRangeParser.parseText("matthew+11:7-30")

    val expected: VerseRange = VerseRange(
      SingleVerse(matthew, 11, 7), SingleVerse(matthew, 11, 30))
    assertVerseRangesEqual(expected, verseRange)
  }


  test("parseText 2 chapters") {
    val verseRange: VerseRange = VerseRangeParser.parseText("genesis+16:1-18:15")

    val expected: VerseRange = VerseRange(
      SingleVerse(genesis, 16, 1), SingleVerse(genesis, 18, 15))
    assertVerseRangesEqual(expected, verseRange)
  }


  test("parseText one verse") {
    val verseRange: VerseRange = VerseRangeParser.parseText("proverbs+10:5")
    val expected: VerseRange = VerseRange(
      SingleVerse(proverbs, 10, 5), SingleVerse(proverbs, 10, 5))
    assertVerseRangesEqual(expected, verseRange)

  }



  test("parseMonthTextFileLine") {
    val verseRanges: List[VerseRange] = VerseRangeParser
      .parseMonthTextFileLine("genesis+39:1-41:16;matthew+12:46-13:23;psalm+17:1-15")

    assertVerseRangesEqual(VerseRange(
      SingleVerse(genesis, 39, 1), SingleVerse(genesis, 41, 16)),
      verseRanges(0))

    assertVerseRangesEqual(VerseRange(
      SingleVerse(matthew, 12, 46), SingleVerse(matthew, 13, 23)),
      verseRanges(1))

    assertVerseRangesEqual(VerseRange(
      SingleVerse(psalms, 17, 1), SingleVerse(psalms, 17, 15)),
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


  test("books") {
    assertEquals(Seq(genesis), VerseRange(SingleVerse(genesis, 1, 1), SingleVerse(genesis, 2, 2)).books)
    assertEquals(Seq(genesis, exodus), VerseRange(SingleVerse(genesis, 1, 1), SingleVerse(exodus, 2, 2)).books)
    assertEquals(Seq(genesis, exodus, leviticus, numbers, deuteronomy), VerseRange(SingleVerse(genesis, 1, 1), SingleVerse(deuteronomy, 2, 2)).books)
    assertEquals(Seq(_3john, jude, revelation), VerseRange(SingleVerse(_3john, 1, 1), SingleVerse(revelation, 2, 2)).books)
  }

  test("rsbNotes") {
    val noteVerseRangeStrings: Seq[String] = VerseRange(SingleVerse(titus, 1, 11), SingleVerse(titus, 1, 13))
      .rsbNotes
      .map(rsbNote => rsbNote.verseRange.toString)

    assertEquals(Seq(
      "titus,1,10-titus,1,16",
      "titus,1,11",
      "titus,1,12",
      "titus,1,13"),
      noteVerseRangeStrings)
  }




}


