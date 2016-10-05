package jsonb.unused

import java.io.InputStream

import scala.io.BufferedSource

/**
  * Reads the month txt files to find the names of all the books
  */
object FindOneYearBibleBookNames {

  /**
    * Returns a tuple containing the list of old testament books and
    * the list of new testament books
    */
  def findAll(): (List[String], List[String]) = {
    val iMonths = 1 to 12

    //  List of file names like: "/months/txt/08.txt"
    val monthFileNames: List[String] = iMonths.map(i => {
      val sMonthNum = if (i < 10) "0" + i else "" + i
      "/months/txt/" + sMonthNum + ".txt"
    }).toList

    findAll(monthFileNames, Nil, Nil)
  }

  private def findAll(monthFileNames: List[String], oldTestamentList: List[String], newTestamentList: List[String])
  : (List[String], List[String]) = {

    monthFileNames match {
      case Nil => (oldTestamentList, newTestamentList)
      case monthFileName :: theRest => {
        val tuple: (List[String], List[String]) = findAll(monthFileName, oldTestamentList, newTestamentList)
        findAll(theRest, tuple._1, tuple._2)
      }
    }
  }

  private def findAll(monthFileName: String, oldTestamentList: List[String], newTestamentList: List[String])
  : (List[String], List[String]) = {
    val inputStream: InputStream = getClass.getResourceAsStream(monthFileName)
    val bufferedSource: BufferedSource = io.Source.fromInputStream(inputStream)
    val lines: Iterator[String] = bufferedSource.getLines()
    findAll(lines, oldTestamentList, newTestamentList)
  }


  private def findAll(lines: Iterator[String], oldTestamentList: List[String], newTestamentList: List[String])
  : (List[String], List[String]) = {

    def addToListIfDifferent(list: List[String], s: String): List[String] =
      if (list.isEmpty || list.last != s)
        list :+ s
      else
        list

    if (lines.hasNext) {
      // read the line and if the books are new, add to list
      val lineResult: LineResult = find(lines.next())
      val oldTestamentList2 = addToListIfDifferent(oldTestamentList, lineResult.oldTestamentBook)
      val newTestamentList2 = addToListIfDifferent(newTestamentList, lineResult.newTestamentBook)
      findAll(lines, oldTestamentList2, newTestamentList2)
    }
    else {
      (oldTestamentList, newTestamentList)
    }

  }


  /**
    * Parse line like:
    *   exodus+29:1-30:10;matthew+26:14-46;psalm+31:19-24;proverbs+8:14-26
    * and return ("exodus", "matthew", Set())
    *
    * We could get lines like this:
    *   genesis+50:1-26;exodus+1:1-2:10;matthew+16:13-17:9;mark+16:13-17:9;psalm+21:1-13;proverbs+5:1-6
    * and return ("exodus", "mark", Set(exodus,matthew))
    */
  private def find(line: String): LineResult= {
    val books: Array[String] = line.split(';')
      .map(s => s.substring(0, s.indexOf('+')))

    LineResult(books(0),
      books(books.length-3),
      books.slice(1, books.length - 3).toSet)
  }

  private case class LineResult(oldTestamentBook: String, newTestamentBook: String, ambiguousBooks: Set[String])



  def main(args: Array[String]) {

    val allBooks: (List[String], List[String]) = findAll()

    // Book("matthew", New)
    allBooks._1
      .map(b => "Book(\""+ b + "\", Old),")
      .foreach(book => println(book))
    println
    allBooks._2
      .map(b => "Book(\""+ b + "\", New),")
      .foreach(book => println(book))



  }
}
