package jsonb

import java.io.InputStream

import jsonb.DayReadingParser._

import scala.collection.immutable.IndexedSeq
import scala.io.BufferedSource

/**
  * Reads the month txt files to find the names of all the books
  */
object FindBookNames {

  private def findAll(): (Set[String], Set[String]) = {
    val iMonths = 1 to 12

    //  List of file names like: "/months/txt/08.txt"
    val monthFileNames: List[String] = iMonths.map(i => {
      val sMonthNum = if (i < 10) "0" + i else "" + i
      "/months/txt/" + sMonthNum + ".txt"
    }).toList

    findAll(monthFileNames, Set.empty[String], Set.empty[String])
  }

  private def findAll(monthFileNames: List[String], oldTestamentSet: Set[String], newTestamentSet: Set[String])
  : (Set[String], Set[String]) = {

    monthFileNames match {
      case Nil => (oldTestamentSet, newTestamentSet)
      case monthFileName :: theRest => {

        (oldTestamentSet, newTestamentSet)
      }
    }

  }

  private def findAll(monthFileName: String, oldTestamentSet: Set[String], newTestamentSet: Set[String])
  : (Set[String], Set[String]) = {
    val inputStream: InputStream = getClass.getResourceAsStream(monthFileName)
    val bufferedSource: BufferedSource = io.Source.fromInputStream(inputStream)
    val lines: Iterator[String] = bufferedSource.getLines()
    findAll(lines, oldTestamentSet, newTestamentSet)

    jlf finish
  }

  private def findAll(lines: Iterator[String], oldTestamentSet: Set[String], newTestamentSet: Set[String])
  : (Set[String], Set[String]) = {

    if (lines.hasNext) {
      val oldAndNew: (String, String) = find(lines.next())
      findAll(lines, oldTestamentSet + oldAndNew._1, newTestamentSet + oldAndNew._2)
    }
    else {
      (oldTestamentSet, newTestamentSet)
    }

  }

  private def find(line: String): (String, String) = {

    ("", "")
  }



  def main(args: Array[String]) {
    print("aaaaaaaaaaaaaa")
  }
}
