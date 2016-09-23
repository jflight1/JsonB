package jsonb.rsb

import java.io.InputStream

import jsonb.{Book, SingleVerse, VerseRange, BookInfo}
import org.apache.commons.io.IOUtils
import org.junit.Assert._
import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner

import scala.util.matching.Regex
import scala.util.matching.Regex.Match


@RunWith(classOf[JUnitRunner])
class RsbNoteWebTest extends FunSuite {

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
        println(d1)
        println(d2)
      case _ =>
    }


  }


  private def verseRange(book: Book, title: String): VerseRange = {
    val twoNumberRegex = ".* (\\d+):(\\d+)$".r
    val threeNumberRegex = ".* (\\d+):(\\d+)-(\\d+)$".r
    val fourNumberRegex = ".* (\\d+):(\\d+)-(\\d+):(\\d+)$".r

    title match {
      case twoNumberRegex(s1, s2) =>
        val chapter = s1.asInstanceOf[Int]
        val verse = s2.asInstanceOf[Int]
        val singleVerse: SingleVerse = SingleVerse(book, chapter, verse)
        VerseRange(singleVerse, singleVerse)

      case threeNumberRegex(s1, s2, s3) =>
        val chapter = s1.asInstanceOf[Int]
        val verse1 = s2.asInstanceOf[Int]
        val verse2 = s3.asInstanceOf[Int]
        VerseRange(SingleVerse(book, chapter, verse1),
          SingleVerse(book, chapter, verse1))

      case fourNumberRegex(s1, s2, s3, s4) =>
        val chapter1 = s1.asInstanceOf[Int]
        val verse1 = s2.asInstanceOf[Int]
        val chapter2 = s4.asInstanceOf[Int]
        val verse2 = s4.asInstanceOf[Int]
        VerseRange(SingleVerse(book, chapter1, verse1),
          SingleVerse(book, chapter2, verse2))

      case _ => throw new Exception("Unparsable title: " + title)
    }

  }


}


