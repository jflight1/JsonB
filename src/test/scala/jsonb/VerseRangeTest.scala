package jsonb

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner


@RunWith(classOf[JUnitRunner])
class VerseRangeTest extends FunSuite {

  test("toJson") {

    val jsonString = VerseRange(VerseLocation(Books.Genesis, 1, 2), VerseLocation(Books.Exodus, 3, 4))
      .toJson

    val expected = "{\r\n  \"start\" : {\r\n    \"book\" : \"genesis\",\r\n    \"chapter\" : 1,\r\n    \"verse\" : 2\r\n  },\r\n  \"end\" : {\r\n    \"book\" : \"exodus\",\r\n    \"chapter\" : 3,\r\n    \"verse\" : 4\r\n  }\r\n}"
    assert(jsonString == expected)
  }


  test("parse") {
    val jsonString: String =
      "{ " +
        "  \"start\" : { " +
        "    \"book\" : \"genesis\", " +
        "    \"chapter\" : 1, " +
        "    \"verse\" : 2 " +
        "  }, " +
        "  \"end\" : { " +
        "    \"book\" : \"exodus\", " +
        "    \"chapter\" : 3, " +
        "    \"verse\" : 4 " +
        "  } " +
        "} "


    val verseRange: VerseRange = VerseRangeParser.parse(jsonString)

    assert(verseRange.start.book == Books.Genesis)
    assert(verseRange.start.chapter == 1)
    assert(verseRange.start.verse == 2)

    assert(verseRange.end.book == Books.Exodus)
    assert(verseRange.end.chapter == 3)
    assert(verseRange.end.verse == 4)
  }



  test("parseText one chapter") {
    val verseRange: VerseRange = VerseRangeParser.parseText("matthew+11:7-30").get

    assert(verseRange.start.book == Books.Matthew)
    assert(verseRange.start.chapter == 11)
    assert(verseRange.start.verse == 7)

    assert(verseRange.end.book == Books.Matthew)
    assert(verseRange.end.chapter == 11)
    assert(verseRange.end.verse == 30)
  }


  test("parseText 2 chapters") {
    val verseRange: VerseRange = VerseRangeParser.parseText("genesis+16:1-18:15").get

    assert(verseRange.start.book == Books.Genesis)
    assert(verseRange.start.chapter == 16)
    assert(verseRange.start.verse == 1)

    assert(verseRange.end.book == Books.Genesis)
    assert(verseRange.end.chapter == 18)
    assert(verseRange.end.verse == 15)
  }

}


