package jsonb

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner


@RunWith(classOf[JUnitRunner])
class VerseLocationTest extends FunSuite {

  test("toJson") {
    val jsonString: String = VerseLocation(Books.Genesis, 3, 4).toJson
    val expected = "{\r\n  \"book\" : \"genesis\",\r\n  \"chapter\" : 3,\r\n  \"verse\" : 4\r\n}";
    assert(jsonString == expected)
  }


  test("parse") {
    val jsonString: String =
      "{ " +
        "  \"book\" : \"genesis\", " +
        "  \"chapter\" : 1, " +
        "  \"verse\" : 2 " +
        "} "

    val verseLocation: VerseLocation = VerseLocationParser.parse(jsonString)

    assert(verseLocation.book == Books.Genesis)
    assert(verseLocation.chapter == 1)
    assert(verseLocation.verse == 2)
  }

}


