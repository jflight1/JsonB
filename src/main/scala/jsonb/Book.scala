package jsonb

import java.io.{InputStream, PrintWriter}

import play.api.libs.functional.syntax._
import play.api.libs.json._

import scala.io.BufferedSource


case class Book(oneYearBibleName: String, exbibName: String, numChapters: Int, isOldTestament: Boolean) {

  def nameMatches(name: String): Boolean = {
    oneYearBibleName.toLowerCase == cleanName(name) ||
      exbibName.toLowerCase == cleanName(name) ||
      (oneYearBibleName == "psalms" && cleanName(name).contains("psalm")) // special case because you say "psalm 23"
  }

  def cleanName(name: String) = name.trim.toLowerCase

}



object BookParser extends JsonParserBase[Book] {

  implicit val bookReads: Reads[Book] = (
    (JsPath \ "oneYearBibleName").read[String] and
      (JsPath \ "exbibName").read[String] and
      (JsPath \ "numChapters").read[Int] and
      (JsPath \ "isOldTestament").read[Boolean]
    )(Book.apply _)


  implicit val bookWrites: Writes[Book] = (
    (JsPath \ "oneYearBibleName").write[String] and
      (JsPath \ "exbibName").write[String] and
      (JsPath \ "numChapters").write[Int] and
      (JsPath \ "isOldTestament").write[Boolean]
    )(unlift(Book.unapply))

  override def toJsObject(book: Book): JsObject = bookWrites.writes(book).as[JsObject]


/*
  override def toJsObject(book: Book): JsObject = Json.obj(
    "oneYearBibleName" -> book.oneYearBibleName,
    "exbibName" -> book.exbibName,
    "numChapters" -> book.numChapters,
    "isOldTestament" -> book.isOldTestament)
*/


  override def fromJson(jsObject: JsObject): Book =
    bookReads.reads(jsObject).get
}





object Books {

  val isOldTestament = true
  val isNewTestament = false

  val allBooks: Seq[Book] = Seq(
    Book("genesis","Gen",50, isOldTestament),
    Book("exodus","Exod",40, isOldTestament),
    Book("leviticus","Lev",27, isOldTestament),
    Book("numbers","Num",36, isOldTestament),
    Book("deuteronomy","Deut",34, isOldTestament),
    Book("joshua","Josh",24, isOldTestament),
    Book("judges","Judg",21, isOldTestament),
    Book("ruth","Ruth",4, isOldTestament),
    Book("1samuel","1Sam",31, isOldTestament),
    Book("2samuel","2Sam",24, isOldTestament),
    Book("1kings","1Kgs",22, isOldTestament),
    Book("2kings","2Kgs",25, isOldTestament),
    Book("1chronicles","1Chr",29, isOldTestament),
    Book("2chronicles","2Chr",36, isOldTestament),
    Book("ezra","Ezra",10, isOldTestament),
    Book("nehemiah","Neh",13, isOldTestament),
    Book("esther","Esth",10, isOldTestament),
    Book("job","Job",42, isOldTestament),
    new Book("psalms","Ps",150, isOldTestament),
    Book("proverbs","Prov",31, isOldTestament),
    Book("ecclesiastes","Eccl",12, isOldTestament),
    Book("song","Song",8, isOldTestament),
    Book("isaiah","Isa",66, isOldTestament),
    Book("jeremiah","Jer",52, isOldTestament),
    Book("lamentations","Lam",5, isOldTestament),
    Book("ezekiel","Ezek",48, isOldTestament),
    Book("daniel","Dan",12, isOldTestament),
    Book("hosea","Hos",14, isOldTestament),
    Book("joel","Joel",3, isOldTestament),
    Book("amos","Amos",9, isOldTestament),
    Book("obadiah","Obad",1, isOldTestament),
    Book("jonah","Jonah",4, isOldTestament),
    Book("micah","Mic",7, isOldTestament),
    Book("nahum","Nah",3, isOldTestament),
    Book("habakkuk","Hab",3, isOldTestament),
    Book("zephaniah","Zeph",3, isOldTestament),
    Book("haggai","Hag",2, isOldTestament),
    Book("zechariah","Zech",14, isOldTestament),
    Book("malachi","Mal",4, isOldTestament),
    Book("matthew","Matt",28, isNewTestament),
    Book("mark","Mark",16, isNewTestament),
    Book("luke","Luke",24, isNewTestament),
    Book("john","John",21, isNewTestament),
    Book("acts","Acts",28, isNewTestament),
    Book("romans","Rom",16, isNewTestament),
    Book("1corinthians","1Cor",16, isNewTestament),
    Book("2corinthians","2Cor",13, isNewTestament),
    Book("galatians","Gal",6, isNewTestament),
    Book("ephesians","Eph",6, isNewTestament),
    Book("philippians","Phil",4, isNewTestament),
    Book("colossians","Col",4, isNewTestament),
    Book("1thessalonians","1Thess",5, isNewTestament),
    Book("2thessalonians","2Thess",3, isNewTestament),
    Book("1timothy","1Tim",6, isNewTestament),
    Book("2timothy","2Tim",4, isNewTestament),
    Book("titus","Titus",3, isNewTestament),
    Book("philemon","Phlm",1, isNewTestament),
    Book("hebrews","Heb",13, isNewTestament),
    Book("james","Jas",5, isNewTestament),
    Book("1peter","1Pet",5, isNewTestament),
    Book("2peter","2Pet",3, isNewTestament),
    Book("1john","1John",5, isNewTestament),
    Book("2john","2John",1, isNewTestament),
    Book("3john","3John",1, isNewTestament),
    Book("jude","Jude",1, isNewTestament),
    Book("revelation","Rev",22, isNewTestament))

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
