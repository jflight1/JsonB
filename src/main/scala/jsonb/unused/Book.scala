package jsonb.unused

import java.io.InputStream

import jsonb.Book

import scala.io.BufferedSource




object Books_old {


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
        val book: Book = Book(oneYearBibleName, exbibName, nivName = "", "", isOldTestament = true, Seq(1)) // bug

        book :: generateBooks(exbibLineIter, oneYearBibleBookNames,
          index + oneYearBibleBookNameInfo.indexIncrement)
      }

      }

  }

}
