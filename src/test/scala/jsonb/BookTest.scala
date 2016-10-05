package jsonb


import jsonb.Assert._
import org.junit.Assert._
import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner


@RunWith(classOf[JUnitRunner])
class BookTest extends FunSuite {



  test("Books.allBooks") {

    val books: Seq[Book] = Books.allBooks
    assertEquals(66, books.size)

    val actualBook: Book = books(7)

    val expectedBook: Book = Book(
      oneYearBibleName = "ruth",
      exbibName = "Ruth",
      nivName = "Ruth",
      isOldTestament = true,
      chapterNumVerses = Seq(22, 23, 18, 22)
    )

    assertBooksEqual(expectedBook, actualBook)
    assertEquals(4, actualBook.numChapters)
  }


}


