package jsonb

import java.io.{InputStream, PrintWriter}

import play.api.libs.functional.syntax._
import play.api.libs.json._

import scala.io.BufferedSource


/**
  * Info about a book.
  */
case class Book(oneYearBibleName: String, exbibName: String, numChapters: Int, isOldTestament: Boolean) {

  def nameMatches(name: String): Boolean = {
    oneYearBibleName.toLowerCase == cleanName(name) ||
      exbibName.toLowerCase == cleanName(name) ||
      (oneYearBibleName == "psalms" && cleanName(name).contains("psalm")) // special case because you say "psalm 23"
  }

  def cleanName(name: String) = name.trim.toLowerCase

}



object BookParser extends JsonParserBase[Book] {

  override def toJsObject(book: Book): JsObject = Json.obj(
    "oneYearBibleName" -> book.oneYearBibleName,
    "exbibName" -> book.exbibName,
    "numChapters" -> book.numChapters,
    "isOldTestament" -> book.isOldTestament)


  override def fromJson(jsObject: JsObject): Book =
    Book(
      (jsObject \ "oneYearBibleName").as[String],
      (jsObject \ "exbibName").as[String],
      (jsObject \ "numChapters").as[Int],
      (jsObject \ "isOldTestament").as[Boolean])


}





object Books {

  lazy val allBooks: Seq[Book] = BookParser.readSeqFromFile("/books.json")


  def find(name: String): Book = {
    val books: Seq[Book] = allBooks.filter(book => book.nameMatches(name))

    if (books == Nil || books.size > 1) {
      throw new Exception("Bad Book name: " + name)
    }

    books.head
  }


}


object BookFactory {

  def generateAllBooks(): List[Book] = {

    // Get One Year Bible book name
    val bookNameLists: (List[String], List[String]) = FindOneYearBibleBookNames.findAll()
    val oneYearBibleBookNames: List[String] = bookNameLists._1 ::: bookNameLists._2


    // get exbib names & num chapters
    val inputStream: InputStream = getClass.getResourceAsStream("/exbib_names.txt")
    val bufferedSource: BufferedSource = io.Source.fromInputStream(inputStream)
    val exbibLineIter: Iterator[String] = bufferedSource.getLines()

    generateBooks(exbibLineIter, oneYearBibleBookNames, 0)
  }



  def generateBooks(exbibLineIter: Iterator[String],
                        oneYearBibleBookNames: List[String],
                        index: Int)
  : List[Book] = {
    if (!exbibLineIter.hasNext) {
      Nil
    }
    else {
      val exbibLine: String = exbibLineIter.next()
      if (exbibLine.isEmpty) Nil

      val exbibLineParts: Array[String] = exbibLine.split(',')
      val exbibName: String = exbibLineParts(0)
      val numChapters: Int = exbibLineParts(1).toInt

      // oneYearBibleBookNames doesn't have psalms and proverbs so we need
      // special handling for those
      case class OneYearBibleBookNameInfo(name: String, indexIncrement: Int)
      val oneYearBibleBookNameInfo: OneYearBibleBookNameInfo =
        if (exbibName == "Ps")  OneYearBibleBookNameInfo("psalms", 0)
        else if (exbibName == "Prov")  OneYearBibleBookNameInfo("proverbs", 0)
        else OneYearBibleBookNameInfo(oneYearBibleBookNames(index), 1)

      val oneYearBibleName: String = oneYearBibleBookNameInfo.name

      val book: Book = Book(oneYearBibleName, exbibName, numChapters, isOldTestament = true) // bug

      book :: generateBooks(exbibLineIter, oneYearBibleBookNames,
        index + oneYearBibleBookNameInfo.indexIncrement)
    }
  }


  def writeBooksToFile() = {
    val json: String = BookParser.seqToJson(Books.allBooks)
    var fileName = "src\\main\\resources\\books.json"
    val printWriter: PrintWriter = new PrintWriter(fileName)
    printWriter.println(json)
    printWriter.close()
  }


  def main(args: Array[String]): Unit = {

    writeBooksToFile()


    //    BookFactory.generateAllBooks()
//      .foreach(b => println("Book(\"" + b.oneYearBibleName + "\",\"" + b.exbibName + "\"," + b.numChapters + "),"))

  }

}
