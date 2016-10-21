package jsonb.rsb

import jsonb.Assert._
import jsonb.{Books, SingleVerse, VerseRange}
import org.junit.Assert._
import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner

import scala.util.matching.Regex


@RunWith(classOf[JUnitRunner])
class RsbNoteTitleParserTest extends FunSuite {


  test("verseRange") {
    val book = Books.find("ruth")

    def test(title: String, chapter1: Int, verse1: Int, chapter2: Int, verse2: Int): Unit = {

      val actualVerseRange = RsbNoteTitleParser.verseRange(title, book)
      val expectedVerseRange: VerseRange = VerseRange(SingleVerse(book, chapter1, verse1), SingleVerse(book, chapter2, verse2))
      assertVerseRangesEqual(expectedVerseRange, actualVerseRange)
    }

    test("Ruth 1:2-3:4", 1, 2, 3, 4)
    test("Ruth 2:3-4:5", 2, 3, 4, 5)

    test("Ruth 1-2:3", 1, 1, 2, 3)
    test("Ruth 2-3:4", 2, 1, 3, 4)

    test("Ruth 1:2-3", 1, 2, 1, 3)
    test("Ruth 2:3-4", 2, 3, 2, 4)

    test("Ruth 1-2", 1, 1, 2, 23)
    test("Ruth 2-3", 2, 1, 3, 18)

    test("Ruth 1", 1, 1, 1, 22)
    test("Ruth 2", 2, 1, 2, 23)
  }


  /*
    1 Sam 22:22
    1 Sam 22:22-33
    1 Sam 22:22-23:33

   */
  test("regex") {
    val form1Strings = Seq("1 Sam 23:45", "Sam 6:7", "Sam 890:333")
    val form1Regex = ".* \\d+:\\d+$"

    val form2Strings = Seq("1 Sam 22:22-23", "Sam 22:2-3")
    val form2Regex = ".* \\d+:\\d+-\\d+$"

    val form3Strings = Seq("1 Sam 22:22-23:23", "Sam 2:2-3:3")
    val form3Regex = ".* \\d+:\\d+-\\d+:\\d+$"

    form1Strings.foreach(s => {
      assertTrue(s.matches(form1Regex))
      assertFalse(s.matches(form2Regex))
      assertFalse(s.matches(form3Regex))
    })

    form2Strings.foreach(s => {
      assertFalse(s.matches(form1Regex))
      assertTrue(s.matches(form2Regex))
      assertFalse(s.matches(form3Regex))
    })

    form3Strings.foreach(s => {
      assertFalse(s.matches(form1Regex))
      assertFalse(s.matches(form2Regex))
      assertTrue(s.matches(form3Regex))
    })
  }

  test("regex 2") {

    val regex: Regex = ".* (\\d+):(\\d+)$".r
    "1 Sam 23:45x" match {
      case regex(d1, d2) =>
        assertEquals(23, d1)
        assertEquals(45, d2)
      case _ =>
    }
  }



}


