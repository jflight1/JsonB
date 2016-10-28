package jsonb.niv

import java.io.PrintWriter

import jsonb.{Book, Utils, Books}
import play.api.libs.json.{Json, JsObject}


/**
  * Reads hgh_niv.json
  */
object HghNivBooks {

  lazy val allBooks: Seq[NivBook] = {
    val json: String = Utils.resourceFileToString("/niv/hgh_niv.json")
    val allBooksJsObject: JsObject = Json.parse(json).as[JsObject]

    Books.allBooks
      .map(book => {
        val bookJsObject: JsObject = (allBooksJsObject \ book.hghNivName).as[JsObject]
        HghNivBookParser(book).fromJson(bookJsObject)
      })
  }


  /**
    * Generates json files for all books
    */
  def writeNivBookFiles(): Unit = {
    allBooks
      .foreach(nivBook => {
        val book: Book = nivBook.book
        println(book.codeName);
        val fileName = {
          val leadingZero = if (book.index < 10) "0" else ""
          "src\\main\\resources\\niv\\" + leadingZero + book.index + "_" + book.codeName + ".json"
        }

        val json: String = NivBookParser.toJson(nivBook)
        val printWriter: PrintWriter = new PrintWriter(fileName)
        printWriter.println(json)
        printWriter.close()
      })

  }


  def main(args: Array[String]): Unit = {
    writeNivBookFiles()

  }

}
