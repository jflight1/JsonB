package jsonb.unused

import java.io.{PrintWriter, InputStream}

import jsonb.rsb.{RsbNote, RsbNoteWebUtils}
import jsonb.rsb.RsbNoteWebUtils._
import jsonb.{JsonParserBase, Books, Book}
import play.api.libs.json.{JsValue, Json, JsObject}

import scala.io.BufferedSource


//
// Everything in this file is for adding rsbNoteName to books.json
//


case class BookV4(book: Book, rsbNoteName: String)


object BookV4Parser extends JsonParserBase[BookV4] {

  override def toJsValue(bookv4: BookV4): JsObject = {
    val book: Book = bookv4.book
    Json.obj(
      "oneYearBibleName" -> book.oneYearBibleName,
      "exbibName" -> book.exbibName,
      "hghNivName" -> book.hghNivName,
      "rsbNoteName" -> bookv4.rsbNoteName,
      "isOldTestament" -> book.isOldTestament,
      "chapterNumVerses" -> Json.toJson(book.chapterNumVerses))
  }

  override def fromJson(jsValue: JsValue): BookV4 = {
    throw new UnsupportedOperationException
  }
}


private object GenerateV4BooksJsonFile {


  private val oneYearBibleNameToRsbNoteName: Map[String, String] = Map(
    "genesis" -> "Gen",
    "exodus" -> "Ex",
    "leviticus" -> "Lev",
    "numbers" -> "Num",
    "deuteronomy" -> "Deut",
    "joshua" -> "Josh",
    "judges" -> "Judges",
    "ruth" -> "Ruth",
    "1samuel" -> "1 Sam",
    "2samuel" -> "2 Sam",
    "1kings" -> "1 Kings",
    "2kings" -> "2 Kgs",
    "1chronicles" -> "1 Chr",
    "2chronicles" -> "2 Chr",
    "ezra" -> "Ezra",
    "nehemiah" -> "Neh",
    "esther" -> "Esth",
    "job" -> "Job",
    "psalms" -> "Ps",
    "proverbs" -> "Pr",
    "ecclesiastes" -> "Eccl",
    "song" -> "Song",
    "isaiah" -> "Isa",
    "jeremiah" -> "Jer",
    "lamentations" -> "Lam",
    "ezekiel" -> "Ezek",
    "daniel" -> "Dan",
    "hosea" -> "Hos",
    "joel" -> "Joel",
    "amos" -> "Amos",
    "obadiah" -> "Obad",
    "jonah" -> "Jonah",
    "micah" -> "Mic",
    "nahum" -> "Nah",
    "habakkuk" -> "Hab",
    "zephaniah" -> "Zeph",
    "haggai" -> "Hag",
    "zechariah" -> "Zech",
    "malachi" -> "Mal",
    "matthew" -> "Matt",
    "mark" -> "Mark",
    "luke" -> "Luke",
    "john" -> "John",
    "acts" -> "Acts",
    "romans" -> "Rom",
    "1corinthians" -> "1 Cor",
    "2corinthians" -> "2 Cor",
    "galatians" -> "Gal",
    "ephesians" -> "Eph",
    "philippians" -> "Phil",
    "colossians" -> "Col",
    "1thessalonians" -> "1 Thess",
    "2thessalonians" -> "2 Thess",
    "1timothy" -> "1 Tim",
    "2timothy" -> "2 Tim",
    "titus" -> "Titus",
    "philemon" -> "Philemon",
    "hebrews" -> "Heb",
    "james" -> "James",
    "1peter" -> "1 Pet",
    "2peter" -> "2 Pet",
    "1john" -> "1 John",
    "2john" -> "2 John",
    "3john" -> "3 John",
    "jude" -> "Jude",
    "revelation" -> "Rev"
  )


  lazy val allBooks: Seq[BookV4] =
  // Map Books to BookV4s
    jsonb.Books.allBooks.map(book => {
      val hghNivName: String = oneYearBibleNameToRsbNoteName.get(book.oneYearBibleName).get
      BookV4(book, hghNivName)
    })



  def writeBooksToFile() = {
    val json: String = BookV4Parser.seqToJson(GenerateV4BooksJsonFile.allBooks)
    val fileName = "src\\main\\resources\\books_v4.json"
    val printWriter: PrintWriter = new PrintWriter(fileName)
    printWriter.println(json)
    printWriter.close()
  }


  def main(args: Array[String]): Unit = {
    writeBooksToFile()
  }
}



/**
  * Generates a mapping from oneYearBibleName rsbNoteName
  */
object RsbNoteBookNames {

  /**
    * returns a rsb note id for the given book. doesn't matter which one
    */
  private def rsbNoteId(book: Book): Long = {
    val inputStream: InputStream = getClass.getResourceAsStream("/rsb/ids/" + book.oneYearBibleName + "_ids.txt")
    try {
      val bufferedSource: BufferedSource = io.Source.fromInputStream(inputStream)
      val lines: Iterator[String] = bufferedSource.getLines()
      lines.next().toLong
    }

    finally {
      inputStream.close()
    }
  }


  private def rsbNoteName(book: Book): String = {
    val id: Long = rsbNoteId(book)
    val rsbNote: RsbNote = RsbNoteWebUtils.rsbNoteFromId(id, book)

    //val regex = ".* (\\d+):(\\d+)$".r
    val regex = "(.*) \\d+.*$".r
    rsbNote.title match {
      case regex(rsbNoteName) => rsbNoteName
      case _ => throw new Exception("Unparsable title: " + rsbNote.title)
    }
  }


  def main(args: Array[String]): Unit = {

    Books.allBooks.foreach(book => {
      println("\"" + book.oneYearBibleName + "\" -> \"" + rsbNoteName(book) + "\",")
    })


  }

  /*

   */

}
