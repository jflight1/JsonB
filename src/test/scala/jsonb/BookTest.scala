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
  }


  test("Book fields") {
    val books: Seq[Book] = Books.allBooks

    // use 1samuel because all the names are different
    val actualBook: Book = books(8)
    val expectedBook: Book = Book(
      oneYearBibleName = "1samuel",
      exbibName = "1Sam",
      nivName = "1 Samuel",
      rsbNoteName = "1 Sam",
      isOldTestament = true,
      chapterNumVerses = Seq(28, 36, 21, 22, 12, 21, 17, 22, 27, 27, 15, 25, 23, 52, 35, 23, 58, 30, 24, 42, 15, 23, 29, 22, 44, 25, 12, 25, 11, 31, 13)
    )

    assertBooksEqual(expectedBook, actualBook)
  }


  test("numChapters") {
    val book: Book = Books.find("ruth")
    assertEquals(4, book.numChapters)
  }


  test("find") {
    Books.allBooks.foreach(book => {
      def check(name: String): Unit = {
        // alter name to check trim and case insensitivity
        assertEquals(book, Books.find(" " + name.toUpperCase + " "))
      }

      check(book.oneYearBibleName)
      check(book.exbibName)
      check(book.nivName)
      check(book.rsbNoteName)

      if (book == Books.find("psalms")) {
        check("psalm") // check special case
      }
    })
  }
}


