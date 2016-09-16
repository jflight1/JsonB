package jsonb

import java.io.InputStream

import scala.io.BufferedSource


case class BookInfo(oneYearBibleName: String, exbibName: String, numChapters: Int)


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

      val bookInfo: BookInfo = BookInfo(oneYearBibleName, exbibName, numChapters)

      bookInfo :: generateBookInfos(exbibLineIter, oneYearBibleBookNames,
        index + oneYearBibleBookNameInfo.indexIncrement)
    }



  }

  val allBookInfos: List[BookInfo] = List(
    BookInfo("genesis","Gen",50),
    BookInfo("exodus","Exod",40),
    BookInfo("leviticus","Lev",27),
    BookInfo("numbers","Num",36),
    BookInfo("deuteronomy","Deut",34),
    BookInfo("joshua","Josh",24),
    BookInfo("judges","Judg",21),
    BookInfo("ruth","Ruth",4),
    BookInfo("1samuel","1Sam",31),
    BookInfo("2samuel","2Sam",24),
    BookInfo("1kings","1Kgs",22),
    BookInfo("2kings","2Kgs",25),
    BookInfo("1chronicles","1Chr",29),
    BookInfo("2chronicles","2Chr",36),
    BookInfo("ezra","Ezra",10),
    BookInfo("nehemiah","Neh",13),
    BookInfo("esther","Esth",10),
    BookInfo("job","Job",42),
    BookInfo("psalms","Ps",150),
    BookInfo("proverbs","Prov",31),
    BookInfo("ecclesiastes","Eccl",12),
    BookInfo("song","Song",8),
    BookInfo("isaiah","Isa",66),
    BookInfo("jeremiah","Jer",52),
    BookInfo("lamentations","Lam",5),
    BookInfo("ezekiel","Ezek",48),
    BookInfo("daniel","Dan",12),
    BookInfo("hosea","Hos",14),
    BookInfo("joel","Joel",3),
    BookInfo("amos","Amos",9),
    BookInfo("obadiah","Obad",1),
    BookInfo("jonah","Jonah",4),
    BookInfo("micah","Mic",7),
    BookInfo("nahum","Nah",3),
    BookInfo("habakkuk","Hab",3),
    BookInfo("zephaniah","Zeph",3),
    BookInfo("haggai","Hag",2),
    BookInfo("zechariah","Zech",14),
    BookInfo("malachi","Mal",4),
    BookInfo("matthew","Matt",28),
    BookInfo("mark","Mark",16),
    BookInfo("luke","Luke",24),
    BookInfo("john","John",21),
    BookInfo("acts","Acts",28),
    BookInfo("romans","Rom",16),
    BookInfo("1corinthians","1Cor",16),
    BookInfo("2corinthians","2Cor",13),
    BookInfo("galatians","Gal",6),
    BookInfo("ephesians","Eph",6),
    BookInfo("philippians","Phil",4),
    BookInfo("colossians","Col",4),
    BookInfo("1thessalonians","1Thess",5),
    BookInfo("2thessalonians","2Thess",3),
    BookInfo("1timothy","1Tim",6),
    BookInfo("2timothy","2Tim",4),
    BookInfo("titus","Titus",3),
    BookInfo("philemon","Phlm",1),
    BookInfo("hebrews","Heb",13),
    BookInfo("james","Jas",5),
    BookInfo("1peter","1Pet",5),
    BookInfo("2peter","2Pet",3),
    BookInfo("1john","1John",5),
    BookInfo("2john","2John",1),
    BookInfo("3john","3John",1),
    BookInfo("jude","Jude",1),
    BookInfo("revelation","Rev",22))


  def main(args: Array[String]): Unit = {

    BookInfoFactory.generateAllBookInfos()
      .foreach(b => println("BookInfo(\"" + b.oneYearBibleName + "\",\"" + b.exbibName + "\"," + b.numChapters + "),"))
/*
*/


    /*
    val tuple: (List[String], List[String]) = FindOneYearBibleBookNames.findAll()
    val strings: List[String] = tuple._1 ::: tuple._2
    strings.foreach(s => println(s))
*/

  }

}
