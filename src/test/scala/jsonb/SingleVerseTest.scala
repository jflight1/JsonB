package jsonb

import org.junit.Assert._
import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner


@RunWith(classOf[JUnitRunner])
class SingleVerseTest extends FunSuite {


  test("toJson") {
    val jsonString: String = SingleVerseParser.toJson(SingleVerse(Books.find("genesis"), 3, 4))
    assertEquals("\"genesis,3,4\"", jsonString)
  }


  test("parse") {
    val jsonString: String =
      "{ " +
        "  \"book\" : \"genesis\", " +
        "  \"chapter\" : 1, " +
        "  \"verse\" : 2 " +
        "} "

    val singleVerse: SingleVerse = SingleVerseParser.fromJson(jsonString)

    assert(singleVerse.book == Books.find("Genesis"))
    assert(singleVerse.chapter == 1)
    assert(singleVerse.verse == 2)
  }

}


