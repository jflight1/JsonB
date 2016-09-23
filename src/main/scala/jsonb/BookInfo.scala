package jsonb

import java.io.InputStream

import jsonb.BookType.BookType

import scala.io.BufferedSource



object BookType extends Enumeration {
  type BookType = Value
  val OldTestament, NewTestament, Psalms, Proverbs = Value
}



case class BookInfo(oneYearBibleName: String, exbibName: String, numChapters: Int, bookType: BookType) {

  def nameMatches(name: String): Boolean = {
    oneYearBibleName.toLowerCase == cleanName(name) ||
      exbibName.toLowerCase == cleanName(name)
  }

  def cleanName(name: String) = name.trim.toLowerCase

}


object BookInfos {

  val OldTestament = BookType.OldTestament
  val NewTestament = BookType.NewTestament

  val allBookInfos: Seq[BookInfo] = Seq(
    BookInfo("genesis","Gen",50, OldTestament),
    BookInfo("exodus","Exod",40, OldTestament),
    BookInfo("leviticus","Lev",27, OldTestament),
    BookInfo("numbers","Num",36, OldTestament),
    BookInfo("deuteronomy","Deut",34, OldTestament),
    BookInfo("joshua","Josh",24, OldTestament),
    BookInfo("judges","Judg",21, OldTestament),
    BookInfo("ruth","Ruth",4, OldTestament),
    BookInfo("1samuel","1Sam",31, OldTestament),
    BookInfo("2samuel","2Sam",24, OldTestament),
    BookInfo("1kings","1Kgs",22, OldTestament),
    BookInfo("2kings","2Kgs",25, OldTestament),
    BookInfo("1chronicles","1Chr",29, OldTestament),
    BookInfo("2chronicles","2Chr",36, OldTestament),
    BookInfo("ezra","Ezra",10, OldTestament),
    BookInfo("nehemiah","Neh",13, OldTestament),
    BookInfo("esther","Esth",10, OldTestament),
    BookInfo("job","Job",42, OldTestament),
    new BookInfo("psalms","Ps",150, BookType.Psalms) {
      override def nameMatches(name: String): Boolean =
      super.nameMatches(name) || cleanName(name) == "psalm"
    },
    BookInfo("proverbs","Prov",31, BookType.Proverbs),
    BookInfo("ecclesiastes","Eccl",12, OldTestament),
    BookInfo("song","Song",8, OldTestament),
    BookInfo("isaiah","Isa",66, OldTestament),
    BookInfo("jeremiah","Jer",52, OldTestament),
    BookInfo("lamentations","Lam",5, OldTestament),
    BookInfo("ezekiel","Ezek",48, OldTestament),
    BookInfo("daniel","Dan",12, OldTestament),
    BookInfo("hosea","Hos",14, OldTestament),
    BookInfo("joel","Joel",3, OldTestament),
    BookInfo("amos","Amos",9, OldTestament),
    BookInfo("obadiah","Obad",1, OldTestament),
    BookInfo("jonah","Jonah",4, OldTestament),
    BookInfo("micah","Mic",7, OldTestament),
    BookInfo("nahum","Nah",3, OldTestament),
    BookInfo("habakkuk","Hab",3, OldTestament),
    BookInfo("zephaniah","Zeph",3, OldTestament),
    BookInfo("haggai","Hag",2, OldTestament),
    BookInfo("zechariah","Zech",14, OldTestament),
    BookInfo("malachi","Mal",4, OldTestament),
    BookInfo("matthew","Matt",28, NewTestament),
    BookInfo("mark","Mark",16, NewTestament),
    BookInfo("luke","Luke",24, NewTestament),
    BookInfo("john","John",21, NewTestament),
    BookInfo("acts","Acts",28, NewTestament),
    BookInfo("romans","Rom",16, NewTestament),
    BookInfo("1corinthians","1Cor",16, NewTestament),
    BookInfo("2corinthians","2Cor",13, NewTestament),
    BookInfo("galatians","Gal",6, NewTestament),
    BookInfo("ephesians","Eph",6, NewTestament),
    BookInfo("philippians","Phil",4, NewTestament),
    BookInfo("colossians","Col",4, NewTestament),
    BookInfo("1thessalonians","1Thess",5, NewTestament),
    BookInfo("2thessalonians","2Thess",3, NewTestament),
    BookInfo("1timothy","1Tim",6, NewTestament),
    BookInfo("2timothy","2Tim",4, NewTestament),
    BookInfo("titus","Titus",3, NewTestament),
    BookInfo("philemon","Phlm",1, NewTestament),
    BookInfo("hebrews","Heb",13, NewTestament),
    BookInfo("james","Jas",5, NewTestament),
    BookInfo("1peter","1Pet",5, NewTestament),
    BookInfo("2peter","2Pet",3, NewTestament),
    BookInfo("1john","1John",5, NewTestament),
    BookInfo("2john","2John",1, NewTestament),
    BookInfo("3john","3John",1, NewTestament),
    BookInfo("jude","Jude",1, NewTestament),
    BookInfo("revelation","Rev",22, NewTestament))

  def find(name: String): BookInfo = {
    val bookInfos: Seq[BookInfo] = allBookInfos.filter(bookInfo => bookInfo.nameMatches(name))

    if (bookInfos == Nil || bookInfos.size > 1) {
      throw new Exception("Bad BookInfo name: " + name)
    }

    bookInfos.head
  }


}


object BookInfoFactory {

  def generateAllBookInfos(): List[BookInfo] = {

    // Get One Year Bible book name
    val bookNameLists: (List[String], List[String]) = FindOneYearBibleBookNames.findAll()
    val oneYearBibleBookNames: List[String] = bookNameLists._1 ::: bookNameLists._2


    // get exbib names & num chapters
    val inputStream: InputStream = getClass.getResourceAsStream("/exbib_names.txt")
    val bufferedSource: BufferedSource = io.Source.fromInputStream(inputStream)
    val exbibLineIter: Iterator[String] = bufferedSource.getLines()

    generateBookInfos(exbibLineIter, oneYearBibleBookNames, 0)
  }



  def generateBookInfos(exbibLineIter: Iterator[String],
                        oneYearBibleBookNames: List[String],
                        index: Int)
  : List[BookInfo] = {
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

      val bookInfo: BookInfo = BookInfo(oneYearBibleName, exbibName, numChapters, BookType.OldTestament) // bug

      bookInfo :: generateBookInfos(exbibLineIter, oneYearBibleBookNames,
        index + oneYearBibleBookNameInfo.indexIncrement)
    }
  }


  def main(args: Array[String]): Unit = {

    BookInfoFactory.generateAllBookInfos()
      .foreach(b => println("BookInfo(\"" + b.oneYearBibleName + "\",\"" + b.exbibName + "\"," + b.numChapters + "),"))

  }

}
