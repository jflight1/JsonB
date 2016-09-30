package jsonb


import jsonb.Assert._
import org.junit.Assert._
import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner


@RunWith(classOf[JUnitRunner])
class BookTest extends FunSuite {



  test("parseMonthFile") {

    val books: Seq[Book] = BookParser.readSeqFromFile("/books.json")


    books.foreach(book =>  println(book))



  }


}


