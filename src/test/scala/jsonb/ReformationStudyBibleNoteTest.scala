package jsonb

import java.io.InputStream

import jsonb.DayReadingParser._
import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner

import org.apache.commons.io.IOUtils

@RunWith(classOf[JUnitRunner])
class ReformationStudyBibleNoteTest extends FunSuite {




  test("jlf") {

    val inputStream: InputStream = getClass.getResourceAsStream("/exbib_osis.json")
    val json: String = IOUtils.toString(inputStream, "UTF-8")

    val ids: List[Long] = ReformationStudyBibleNoteFactory.getNoteIds(json)

    ids.foreach(f => println(f))
//    println(string)

  }





  //////////////////////////    junk


  test("toJson") {
    val jsonString: String = SingleVerse(Books.Genesis, 3, 4).toJson
    val expected = "{\r\n  \"book\" : \"genesis\",\r\n  \"chapter\" : 3,\r\n  \"verse\" : 4\r\n}"
    assert(jsonString == expected)
  }


  test("parse") {
    val jsonString: String =
      "{ " +
        "  \"book\" : \"genesis\", " +
        "  \"chapter\" : 1, " +
        "  \"verse\" : 2 " +
        "} "

    val singleVerse: SingleVerse = SingleVerseParser.parse(jsonString)

    assert(singleVerse.book == Books.Genesis)
    assert(singleVerse.chapter == 1)
    assert(singleVerse.verse == 2)
  }

}


