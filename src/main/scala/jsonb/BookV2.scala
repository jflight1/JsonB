package jsonb

import java.io.PrintWriter

import play.api.libs.json.{Json, JsObject}


/**
  * Created by jflight on 9/30/2016.
  */
case class BookV2(book: Book, nivName: String)


object BookV2Parser extends JsonParserBase[BookV2] {

  override def toJsObject(bookv2: BookV2): JsObject = {
    val book: Book = bookv2.book
    Json.obj(
      "oneYearBibleName" -> book.oneYearBibleName,
      "exbibName" -> book.exbibName,
      "nivName" -> bookv2.nivName,
      "numChapters" -> book.numChapters,
      "isOldTestament" -> book.isOldTestament)
  }

  override def fromJson(jsObject: JsObject): BookV2 = {
    val book = Book(
      (jsObject \ "oneYearBibleName").as[String],
      (jsObject \ "exbibName").as[String],
      (jsObject \ "numChapters").as[Int],
      (jsObject \ "isOldTestament").as[Boolean])

    BookV2(book, (jsObject \ "nivName").as[String])
  }
}


private object BookV2Utils {


  private val oneYearBibleNameToNivName: Map[String, String] = Map(
    "1chronicles" -> "1 Chronicles",
    "1corinthians" -> "1 Corinthians",
    "1john" -> "1 John",
    "1kings" -> "1 Kings",
    "1peter" -> "1 Peter",
    "1samuel" -> "1 Samuel",
    "1thessalonians" -> "1 Thessalonians",
    "1timothy" -> "1 Timothy",
    "2chronicles" -> "2 Chronicles",
    "2corinthians" -> "2 Corinthians",
    "2john" -> "2 John",
    "2kings" -> "2 Kings",
    "2peter" -> "2 Peter",
    "2samuel" -> "2 Samuel",
    "2thessalonians" -> "2 Thessalonians",
    "2timothy" -> "2 Timothy",
    "3john" -> "3 John",
    "acts" -> "Acts",
    "amos" -> "Amos",
    "colossians" -> "Colossians",
    "daniel" -> "Daniel",
    "deuteronomy" -> "Deuteronomy",
    "ecclesiastes" -> "Ecclesiastes",
    "ephesians" -> "Ephesians",
    "esther" -> "Esther",
    "exodus" -> "Exodus",
    "ezekiel" -> "Ezekiel",
    "ezra" -> "Ezra",
    "galatians" -> "Galatians",
    "genesis" -> "Genesis",
    "habakkuk" -> "Habakkuk",
    "haggai" -> "Haggai",
    "hebrews" -> "Hebrews",
    "hosea" -> "Hosea",
    "isaiah" -> "Isaiah",
    "james" -> "James",
    "jeremiah" -> "Jeremiah",
    "job" -> "Job",
    "joel" -> "Joel",
    "john" -> "John",
    "jonah" -> "Jonah",
    "joshua" -> "Joshua",
    "jude" -> "Jude",
    "judges" -> "Judges",
    "lamentations" -> "Lamentations",
    "leviticus" -> "Leviticus",
    "luke" -> "Luke",
    "malachi" -> "Malachi",
    "mark" -> "Mark",
    "matthew" -> "Matthew",
    "micah" -> "Micah",
    "nahum" -> "Nahum",
    "nehemiah" -> "Nehemiah",
    "numbers" -> "Numbers",
    "obadiah" -> "Obadiah",
    "philemon" -> "Philemon",
    "philippians" -> "Philippians",
    "proverbs" -> "Proverbs",
    "psalms" -> "Psalms",
    "revelation" -> "Revelation",
    "romans" -> "Romans",
    "ruth" -> "Ruth",
    "song" -> "Song of Solomon",
    "titus" -> "Titus",
    "zechariah" -> "Zechariah",
    "zephaniah" -> "Zephaniah"
  )

  lazy val allBooks: Seq[BookV2] =
    // Map Books to BookV2s
    Books.allBooks.map(book => {
      val nivName: String = oneYearBibleNameToNivName.get(book.oneYearBibleName).get
      BookV2(book, nivName)
    })



  def writeBooksToFile() = {
    val json: String = BookV2Parser.seqToJson(BookV2Utils.allBooks)
    var fileName = "src\\main\\resources\\books_v2.json"
    val printWriter: PrintWriter = new PrintWriter(fileName)
    printWriter.println(json)
    printWriter.close()
  }


  def main(args: Array[String]): Unit = {

    writeBooksToFile()

  }


}