package jsonb.niv

import jsonb.{Utils, Books}
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


  def main(args: Array[String]): Unit = {
    val nivBook: NivBook = allBooks.filter(nivBook => nivBook.book.codeName == "romans").head

    val json: String = NivBookParser.toJson(nivBook)
//    println(json)

    val nivBook2: NivBook = NivBookParser.fromJson(json)

    println("---------> " + (nivBook == nivBook2))
  }

}
