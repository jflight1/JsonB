package jsonb

import java.io.{InputStream, PrintWriter}

import jsonb.niv.{NivBookParser, NivBook}
import jsonb.rsb.{RsbNoteJsonParser, RsbNote}
import play.api.libs.json._

import scala.io.BufferedSource



/**
  * @param index Canonical index number; starts at 1.
  * @param oneYearBibleName Name in oneyearbibleonline.com.  http://oneyearbibleonline.com/...
  *                         Also this is the name in the daily reading files: \resources\day_reading\txt\*.txt
  * @param exbibName Name used in the exbib api.  https://www.biblegateway.com/exbib/contents/?osis=...
  * @param hghNivName Name in hgh_niv.json
  * @param chapterNumVerses The number of verses in each chapter
  */
case class Book(index: Int, oneYearBibleName: String, exbibName: String, hghNivName: String, rsbNoteName: String,
                isOldTestament: Boolean, chapterNumVerses: Seq[Int]) {

  /**
    * This is the name we used to identify the book in json, file names, code, etc.
    */
  val codeName = oneYearBibleName

  /**
    * name can match any of the names
    */
  def nameMatches(name: String): Boolean = {

    def nameMatches(name: String, okNames: Seq[String]): Boolean = {
      okNames.map(okName => cleanName(okName)) match {
        case Nil => false;
        case `name` :: theRest => true
        case okName :: theRest => nameMatches(name, theRest)
      }
    }

    nameMatches(cleanName(name), Seq(oneYearBibleName, exbibName, hghNivName, rsbNoteName)) ||
      (cleanName(name) == "psalm" && oneYearBibleName == "psalms") // need special case
  }

  private def cleanName(name: String) = name.trim.toLowerCase

  def numChapters: Int = chapterNumVerses.size

  /**
    * The next book after this one in canonical order
    *
    * @throws Exception if you call it with revelation
    */
  lazy val next: Book = Books.nextBookMap(this)

  lazy val prev: Book = Books.prevBookMap(this)


  lazy val nivBook: NivBook = NivBookParser.fromFile(this)

  lazy val rsbNotes: Seq[RsbNote] = RsbNoteJsonParser.fromFile(this)


  def <(that: Book) = this.index < that.index
  def <=(that: Book) = this.index <= that.index
  def >(that: Book) = this.index > that.index
  def >=(that: Book) = this.index >= that.index

  override def toString: String = codeName
}


object BookParser extends JsonParserBase[Book] {

  override def toJsValue(book: Book): JsObject = Json.obj(
    "index" -> book.index,
    "oneYearBibleName" -> book.oneYearBibleName,
    "exbibName" -> book.exbibName,
    "hghNivName" -> book.hghNivName,
    "rsbNoteName" -> book.rsbNoteName,
    "isOldTestament" -> book.isOldTestament,
    "chapterNumVerses" -> Json.toJson(book.chapterNumVerses))


  override def fromJson(jsValue: JsValue): Book =
    Book(
      (jsValue \ "index").as[Int],
      (jsValue \ "oneYearBibleName").as[String],
      (jsValue \ "exbibName").as[String],
      (jsValue \ "hghNivName").as[String],
      (jsValue \ "rsbNoteName").as[String],
      (jsValue \ "isOldTestament").as[Boolean],
      (jsValue \ "chapterNumVerses").as[Seq[Int]])
}


object Books {

  val genesis = Books.find("genesis")
  val exodus = Books.find("exodus")
  val leviticus = Books.find("leviticus")
  val numbers = Books.find("numbers")
  val deuteronomy = Books.find("deuteronomy")
  val joshua = Books.find("joshua")
  val judges = Books.find("judges")
  val ruth = Books.find("ruth")
  val _1samuel = Books.find("1samuel")
  val _2samuel = Books.find("2samuel")
  val _1kings = Books.find("1kings")
  val _2kings = Books.find("2kings")
  val _1chronicles = Books.find("1chronicles")
  val _2chronicles = Books.find("2chronicles")
  val ezra = Books.find("ezra")
  val nehemiah = Books.find("nehemiah")
  val esther = Books.find("esther")
  val job = Books.find("job")
  val psalms = Books.find("psalms")
  val proverbs = Books.find("proverbs")
  val ecclesiastes = Books.find("ecclesiastes")
  val song = Books.find("song")
  val isaiah = Books.find("isaiah")
  val jeremiah = Books.find("jeremiah")
  val lamentations = Books.find("lamentations")
  val ezekiel = Books.find("ezekiel")
  val daniel = Books.find("daniel")
  val hosea = Books.find("hosea")
  val joel = Books.find("joel")
  val amos = Books.find("amos")
  val obadiah = Books.find("obadiah")
  val jonah = Books.find("jonah")
  val micah = Books.find("micah")
  val nahum = Books.find("nahum")
  val habakkuk = Books.find("habakkuk")
  val zephaniah = Books.find("zephaniah")
  val haggai = Books.find("haggai")
  val zechariah = Books.find("zechariah")
  val malachi = Books.find("malachi")
  val matthew = Books.find("matthew")
  val mark = Books.find("mark")
  val luke = Books.find("luke")
  val john = Books.find("john")
  val acts = Books.find("acts")
  val romans = Books.find("romans")
  val _1corinthians = Books.find("1corinthians")
  val _2corinthians = Books.find("2corinthians")
  val galatians = Books.find("galatians")
  val ephesians = Books.find("ephesians")
  val philippians = Books.find("philippians")
  val colossians = Books.find("colossians")
  val _1thessalonians = Books.find("1thessalonians")
  val _2thessalonians = Books.find("2thessalonians")
  val _1timothy = Books.find("1timothy")
  val _2timothy = Books.find("2timothy")
  val titus = Books.find("titus")
  val philemon = Books.find("philemon")
  val hebrews = Books.find("hebrews")
  val james = Books.find("james")
  val _1peter = Books.find("1peter")
  val _2peter = Books.find("2peter")
  val _1john = Books.find("1john")
  val _2john = Books.find("2john")
  val _3john = Books.find("3john")
  val jude = Books.find("jude")
  val revelation = Books.find("revelation")


  /**
    * All books in canonical order
    */
  lazy val allBooks: Seq[Book] = BookParser.readSeqFromFile("/books.json")

  def find(name: String): Book = {
    val books: Seq[Book] = allBooks.filter(book => book.nameMatches(name))

    if (books == Nil || books.size > 1) {
      throw new Exception("Bad Book name: " + name)
    }

    books.head
  }

  def allBooksSortedLargestToSmallest: Seq[Book] = {
    Books.allBooks
      .sortBy(b => -b.chapterNumVerses.sum)
  }

  /**
    * Map one book to the next
    */
  lazy val nextBookMap = (0 until Books.allBooks.size - 1)
    .map(i => allBooks(i) -> allBooks(i + 1))
    .toMap


  /**
    * Map one book to the previous
    */
  lazy val prevBookMap = (Books.allBooks.size - 1 until 0 by -1)
    .map(i => allBooks(i) -> allBooks(i - 1))
    .toMap



  def main(args: Array[String]): Unit = {
    allBooks.foreach(book => {
      println("val " + book.codeName + " = Books.find(\"" + book.codeName + "\")")
    })
  }

}


