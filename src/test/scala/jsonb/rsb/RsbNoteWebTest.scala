package jsonb.rsb

import jsonb.{Book, SingleVerse, VerseRange}
import org.junit.Assert._
import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner

import scala.util.matching.Regex


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


}


