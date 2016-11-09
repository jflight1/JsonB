package jsonb

import jsonb.Books._
import jsonb.Assert._
import jsonb.rsb.RsbNote
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


  private val titus_1_12: VerseRange = VerseRange(SingleVerse(titus, 1, 12), SingleVerse(titus, 1, 12))
  private val titus_1_11_12: VerseRange = VerseRange(SingleVerse(titus, 1, 11), SingleVerse(titus, 1, 12))
  private val titus_1_11_13: VerseRange = VerseRange(SingleVerse(titus, 1, 11), SingleVerse(titus, 1, 13))
  private val titus_1_10_12: VerseRange = VerseRange(SingleVerse(titus, 1, 10), SingleVerse(titus, 1, 12))
  private val titus_1_12_14: VerseRange = VerseRange(SingleVerse(titus, 1, 12), SingleVerse(titus, 1, 14))
  private val titus_1_14_16: VerseRange = VerseRange(SingleVerse(titus, 1, 14), SingleVerse(titus, 1, 16))

  test("intersects") {
    assertTrue(titus_1_12.intersects(titus_1_12))

    assertTrue(titus_1_12.intersects(titus_1_11_13))
    assertTrue(titus_1_11_13.intersects(titus_1_12))

    assertTrue(titus_1_11_13.intersects(titus_1_10_12))
    assertTrue(titus_1_10_12.intersects(titus_1_11_13))

    assertTrue(titus_1_10_12.intersects(titus_1_12_14))
    assertTrue(titus_1_12_14.intersects(titus_1_10_12))

    assertFalse(titus_1_10_12.intersects(titus_1_14_16))
    assertFalse(titus_1_14_16.intersects(titus_1_10_12))
  }


  test("intersection") {
    assertEquals(titus_1_12, titus_1_12.intersection(titus_1_12).get)

    assertEquals(titus_1_12, titus_1_12.intersection(titus_1_11_13).get)
    assertEquals(titus_1_12, titus_1_11_13.intersection(titus_1_12).get)

    assertEquals(titus_1_11_12, titus_1_11_13.intersection(titus_1_10_12).get)
    assertEquals(titus_1_11_12, titus_1_10_12.intersection(titus_1_11_13).get)

    assertEquals(titus_1_12, titus_1_10_12.intersection(titus_1_12_14).get)
    assertEquals(titus_1_12, titus_1_12_14.intersection(titus_1_10_12).get)

    assertEquals(None, titus_1_10_12.intersection(titus_1_14_16))
    assertEquals(None, titus_1_14_16.intersection(titus_1_10_12))
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


  // convert versesWithNotes results to strings for easier testing
  private def sVersesWithNotes(verseRange: VerseRange): Seq[(String, Seq[String])] = {
    val versesWithNotes: Seq[(SingleVerse, Seq[RsbNote])] = verseRange.versesWithNotes
    versesWithNotes.map(tuple => {
      val sRsbNotes: Seq[String] = tuple._2.map(rsbNote => rsbNote.verseRange.toString)
      (tuple._1.toString, sRsbNotes)
    })
  }

  test("versesWithNotes") {

    assertEquals(Seq(
      ("titus,1,11", Seq("titus,1,10-titus,1,16", "titus,1,11")),
      ("titus,1,12", Seq("titus,1,12")),
      ("titus,1,13", Seq("titus,1,13"))),
      sVersesWithNotes(VerseRange(SingleVerse(titus, 1, 11), SingleVerse(titus, 1, 13))))

    assertEquals(Seq(
      ("titus,2,5", Seq("titus,2,1-titus,2,15", "titus,2,2-titus,2,6", "titus,2,5")),
      ("titus,2,6", Nil),
      ("titus,2,7", Seq("titus,2,7"))),
      sVersesWithNotes(VerseRange(SingleVerse(titus, 2, 5), SingleVerse(titus, 2, 7))))

    assertEquals(Seq(
      ("titus,3,15", Seq("titus,3,12-titus,3,15", "titus,3,15")),
      ("philemon,1,1", Seq("philemon,1,1")),
      ("philemon,1,2", Nil)),
      sVersesWithNotes(VerseRange(SingleVerse(titus, 3, 15), SingleVerse(philemon, 1, 2))))

    assertEquals(Seq(
      ("titus,3,15", Seq("titus,3,12-titus,3,15", "titus,3,15")),
      ("philemon,1,1", Seq("philemon,1,1")),
      ("philemon,1,2", Nil),
      ("philemon,1,3", Nil),
      ("philemon,1,4", Nil),
      ("philemon,1,5", Nil),
      ("philemon,1,6", Seq("philemon,1,6")),
      ("philemon,1,7", Seq("philemon,1,7")),
      ("philemon,1,8", Nil),
      ("philemon,1,9", Seq("philemon,1,9")),
      ("philemon,1,10", Seq("philemon,1,10")),
      ("philemon,1,11", Seq("philemon,1,11")),
      ("philemon,1,12", Nil),
      ("philemon,1,13", Nil),
      ("philemon,1,14", Seq("philemon,1,14")),
      ("philemon,1,15", Nil),
      ("philemon,1,16", Seq("philemon,1,16")),
      ("philemon,1,17", Seq("philemon,1,17")),
      ("philemon,1,18", Nil),
      ("philemon,1,19", Seq("philemon,1,19")),
      ("philemon,1,20", Nil),
      ("philemon,1,21", Nil),
      ("philemon,1,22", Seq("philemon,1,22")),
      ("philemon,1,23", Nil),
      ("philemon,1,24", Nil),
      ("philemon,1,25", Nil),
      ("hebrews,1,1", Seq("hebrews,1,1-hebrews,1,4", "hebrews,1,1"))),
      sVersesWithNotes(VerseRange(SingleVerse(titus, 3, 15), SingleVerse(hebrews, 1 ,1))))
  }
}


