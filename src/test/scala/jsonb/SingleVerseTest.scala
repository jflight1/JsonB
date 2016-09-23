package jsonb

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner


@RunWith(classOf[JUnitRunner])
class SingleVerseTest extends FunSuite {


  test("toJson") {
    val jsonString: String = SingleVerseParser.toJson(SingleVerse(BookInfos.find("genesis"), 3, 4))
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

    val singleVerse: SingleVerse = SingleVerseParser.fromJson(jsonString)

    assert(singleVerse.book == BookInfos.find("Genesis"))
    assert(singleVerse.chapter == 1)
    assert(singleVerse.verse == 2)
  }

}


