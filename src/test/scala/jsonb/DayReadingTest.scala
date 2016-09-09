package jsonb

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import org.junit.Assert._

import scala.io.Source


@RunWith(classOf[JUnitRunner])
class DayReadingTest extends FunSuite {

  test("parseMonthFile") {



    val dayReadings: List[DayReading] = DayReadingParser.parseMonthFile("/todo.txt", 1)

//    val dayReadings: List[DayReading] = DayReadingParser.parseMonthFile("/months/01.txt", 1)

    assertEquals(31, dayReadings.size)

  }

}


