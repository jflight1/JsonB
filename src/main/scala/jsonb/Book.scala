package jsonb

import java.io.{InputStream, PrintWriter}

import play.api.libs.json._

import scala.io.BufferedSource



/**
  *
  * @param oneYearBibleName Name in oneyearbibleonline.com.  http://oneyearbibleonline.com/...
  *                         Also this is the name in the daily reading files: \resources\months\txt\*.txt
  * @param exbibName Name used in the exbib api.  https://www.biblegateway.com/exbib/contents/?osis=...
  * @param nivName Name in NIV.json
  * @param chapterNumVerses The number of verses in each chapter
  */
case class Book(oneYearBibleName: String, exbibName: String, nivName: String, isOldTestament: Boolean, chapterNumVerses: Seq[Int]) {

  def nameMatches(name: String): Boolean = {
    oneYearBibleName.toLowerCase == cleanName(name) ||
      exbibName.toLowerCase == cleanName(name) ||
      (oneYearBibleName == "psalms" && cleanName(name).contains("psalm")) // special case because you say "psalm 23"
  }

  def cleanName(name: String) = name.trim.toLowerCase

  def numChapters: Int = chapterNumVerses.size

}


object BookParser extends JsonParserBase[Book] {

  override def toJsObject(book: Book): JsObject = Json.obj(
    "oneYearBibleName" -> book.oneYearBibleName,
    "exbibName" -> book.exbibName,
    "nivName" -> book.nivName,
    "isOldTestament" -> book.isOldTestament,
    "chapterNumVerses" -> Json.toJson(book.chapterNumVerses))


  override def fromJson(jsObject: JsObject): Book =
    Book(
      (jsObject \ "oneYearBibleName").as[String],
      (jsObject \ "exbibName").as[String],
      (jsObject \ "nivName").as[String],
      (jsObject \ "isOldTestament").as[Boolean],
      (jsObject \ "chapterNumVerses").as[Seq[Int]])
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


  def writeBooksToFile() = {
    val json: String = BookParser.seqToJson(Books.allBooks)
    val fileName = "src\\main\\resources\\books.json"
    val printWriter: PrintWriter = new PrintWriter(fileName)
    printWriter.println(json)
    printWriter.close()
  }


  def main(args: Array[String]): Unit = {

    writeBooksToFile()
  }




  /////////////////////////  deprecated

  /**
    * @deprecated This no longer works
    */
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



  /**
    * @deprecated This no longer works
    */
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

      else {
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

        // this no longer works as nivName and isOldTestament are wrong
        val book: Book = Book(oneYearBibleName, exbibName, nivName = "", isOldTestament = true, Seq(1)) // bug

        book :: generateBooks(exbibLineIter, oneYearBibleBookNames,
          index + oneYearBibleBookNameInfo.indexIncrement)
      }

      }

  }

}
