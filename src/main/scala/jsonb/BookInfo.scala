package jsonb

import java.io.InputStream

import jsonb.FindOneYearBibleBookNames._

import scala.io.BufferedSource

/**
  * Created by jflight on 9/14/2016.
  */
case class BookInfo(oneYearBibleName: String, exbibName: String, numChapters: Int) {





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

      val bookInfo: BookInfo = BookInfo(oneYearBibleName, exbibName, numChapters)

      bookInfo :: generateBookInfos(exbibLineIter, oneYearBibleBookNames,
        index + oneYearBibleBookNameInfo.indexIncrement)
    }



  }



  def main(args: Array[String]): Unit = {

    BookInfoFactory.generateAllBookInfos()
      .foreach(b => println(b))
/*
*/


    /*
    val tuple: (List[String], List[String]) = FindOneYearBibleBookNames.findAll()
    val strings: List[String] = tuple._1 ::: tuple._2
    strings.foreach(s => println(s))
*/

  }

}
