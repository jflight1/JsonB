package jsonb

import java.io.InputStream

import jsonb.BookType.BookType

import scala.io.BufferedSource



object BookType extends Enumeration {
  type BookType = Value
  val OldTestament, NewTestament, Psalms, Proverbs = Value
}



case class Book(oneYearBibleName: String, exbibName: String, numChapters: Int, bookType: BookType) {

  def nameMatches(name: String): Boolean = {
    oneYearBibleName.toLowerCase == cleanName(name) ||
      exbibName.toLowerCase == cleanName(name)
  }

  def cleanName(name: String) = name.trim.toLowerCase

}


object Books {

  val OldTestament = BookType.OldTestament
  val NewTestament = BookType.NewTestament

  val allBooks: Seq[Book] = Seq(
    Book("genesis","Gen",50, OldTestament),
    Book("exodus","Exod",40, OldTestament),
    Book("leviticus","Lev",27, OldTestament),
    Book("numbers","Num",36, OldTestament),
    Book("deuteronomy","Deut",34, OldTestament),
    Book("joshua","Josh",24, OldTestament),
    Book("judges","Judg",21, OldTestament),
    Book("ruth","Ruth",4, OldTestament),
    Book("1samuel","1Sam",31, OldTestament),
    Book("2samuel","2Sam",24, OldTestament),
    Book("1kings","1Kgs",22, OldTestament),
    Book("2kings","2Kgs",25, OldTestament),
    Book("1chronicles","1Chr",29, OldTestament),
    Book("2chronicles","2Chr",36, OldTestament),
    Book("ezra","Ezra",10, OldTestament),
    Book("nehemiah","Neh",13, OldTestament),
    Book("esther","Esth",10, OldTestament),
    Book("job","Job",42, OldTestament),
    new Book("psalms","Ps",150, BookType.Psalms) {
      override def nameMatches(name: String): Boolean =
      super.nameMatches(name) || cleanName(name) == "psalm"
    },
    Book("proverbs","Prov",31, BookType.Proverbs),
    Book("ecclesiastes","Eccl",12, OldTestament),
    Book("song","Song",8, OldTestament),
    Book("isaiah","Isa",66, OldTestament),
    Book("jeremiah","Jer",52, OldTestament),
    Book("lamentations","Lam",5, OldTestament),
    Book("ezekiel","Ezek",48, OldTestament),
    Book("daniel","Dan",12, OldTestament),
    Book("hosea","Hos",14, OldTestament),
    Book("joel","Joel",3, OldTestament),
    Book("amos","Amos",9, OldTestament),
    Book("obadiah","Obad",1, OldTestament),
    Book("jonah","Jonah",4, OldTestament),
    Book("micah","Mic",7, OldTestament),
    Book("nahum","Nah",3, OldTestament),
    Book("habakkuk","Hab",3, OldTestament),
    Book("zephaniah","Zeph",3, OldTestament),
    Book("haggai","Hag",2, OldTestament),
    Book("zechariah","Zech",14, OldTestament),
    Book("malachi","Mal",4, OldTestament),
    Book("matthew","Matt",28, NewTestament),
    Book("mark","Mark",16, NewTestament),
    Book("luke","Luke",24, NewTestament),
    Book("john","John",21, NewTestament),
    Book("acts","Acts",28, NewTestament),
    Book("romans","Rom",16, NewTestament),
    Book("1corinthians","1Cor",16, NewTestament),
    Book("2corinthians","2Cor",13, NewTestament),
    Book("galatians","Gal",6, NewTestament),
    Book("ephesians","Eph",6, NewTestament),
    Book("philippians","Phil",4, NewTestament),
    Book("colossians","Col",4, NewTestament),
    Book("1thessalonians","1Thess",5, NewTestament),
    Book("2thessalonians","2Thess",3, NewTestament),
    Book("1timothy","1Tim",6, NewTestament),
    Book("2timothy","2Tim",4, NewTestament),
    Book("titus","Titus",3, NewTestament),
    Book("philemon","Phlm",1, NewTestament),
    Book("hebrews","Heb",13, NewTestament),
    Book("james","Jas",5, NewTestament),
    Book("1peter","1Pet",5, NewTestament),
    Book("2peter","2Pet",3, NewTestament),
    Book("1john","1John",5, NewTestament),
    Book("2john","2John",1, NewTestament),
    Book("3john","3John",1, NewTestament),
    Book("jude","Jude",1, NewTestament),
    Book("revelation","Rev",22, NewTestament))

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

      val book: Book = Book(oneYearBibleName, exbibName, numChapters, BookType.OldTestament) // bug

      book :: generateBooks(exbibLineIter, oneYearBibleBookNames,
        index + oneYearBibleBookNameInfo.indexIncrement)
    }
  }


  def main(args: Array[String]): Unit = {

    BookFactory.generateAllBooks()
      .foreach(b => println("Book(\"" + b.oneYearBibleName + "\",\"" + b.exbibName + "\"," + b.numChapters + "),"))

  }

}
