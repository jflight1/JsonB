package jsonb

import jsonb.Assert._
import org.junit.Assert._
import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import play.api.libs.json.{JsArray, JsObject, Json}


@RunWith(classOf[JUnitRunner])
class DayReadingTest extends FunSuite {

  test("parseMonthFile") {
    val dayReadings: List[DayReading] = DayReadingParser.parseMonthFile("/months/txt/01.txt", 1)

    assertEquals(31, dayReadings.size)

    // genesis+1:1-2:25;matthew+1:1-2:12;psalm+1:1-1:6;proverbs+1:1-1:6
    assertDayReadingsEqual(
      DayReading(1, 1,
        VerseRange(VerseLocation(Books.Genesis, 1, 1), VerseLocation(Books.Genesis, 2, 25)),
        VerseRange(VerseLocation(Books.Matthew, 1, 1), VerseLocation(Books.Matthew, 2, 12)),
        VerseRange(VerseLocation(Books.Psalms, 1, 1), VerseLocation(Books.Psalms, 1, 6)),
        VerseRange(VerseLocation(Books.Proverbs, 1, 1), VerseLocation(Books.Proverbs, 1, 6))),
      dayReadings(0))

    // genesis+50:1-26;exodus+1:1-2:10;matthew+16:13-17:9;psalm+21:1-13;proverbs+5:1-6
    assertDayReadingsEqual(
      DayReading(1, 25,
        VerseRange(VerseLocation(Books.Genesis, 50, 1), VerseLocation(Books.Exodus, 2, 10)),
        VerseRange(VerseLocation(Books.Matthew, 16, 13), VerseLocation(Books.Matthew, 17, 9)),
        VerseRange(VerseLocation(Books.Psalms, 21, 1), VerseLocation(Books.Psalms, 21, 13)),
        VerseRange(VerseLocation(Books.Proverbs, 5, 1), VerseLocation(Books.Proverbs, 5, 6))),
      dayReadings(24))

    // exodus+12:14-13:16;matthew+20:29-21:22;psalm+25:16-22;proverbs+6:12-15
    assertDayReadingsEqual(
      DayReading(1, 31,
        VerseRange(VerseLocation(Books.Exodus, 12, 14), VerseLocation(Books.Exodus, 13, 16)),
        VerseRange(VerseLocation(Books.Matthew, 20, 29), VerseLocation(Books.Matthew, 21, 22)),
        VerseRange(VerseLocation(Books.Psalms, 25, 16), VerseLocation(Books.Psalms, 25, 22)),
        VerseRange(VerseLocation(Books.Proverbs, 6, 12), VerseLocation(Books.Proverbs, 6, 15))),
      dayReadings(30))


    val jsObjects: List[JsObject] = dayReadings.map(dayReading => dayReading.toJsObject)

    val jsArray: JsArray = Json.arr(jsObjects)
    val json: String = Json.prettyPrint(jsArray)
    print(json)

  }

}


