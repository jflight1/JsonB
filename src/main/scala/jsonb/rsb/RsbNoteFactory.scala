package jsonb.rsb

import java.io.{InputStream, PrintWriter}
import java.util.Date

import jsonb.DayReadingParser._
import jsonb._
import play.api.libs.json._

import scala.io.{BufferedSource, Source}


/**
  * Various methods related to creating RsbNotes
  */
object RsbNoteFactory {


  /**
    * Get RsbNotes for a whole book
    */
  def rsbNotes(book: Book): Seq[RsbNote] = {

    val inputStream: InputStream = getClass.getResourceAsStream("/rsb/ids/" + book.oneYearBibleName + "_ids.txt")
    try {
      val bufferedSource: BufferedSource = io.Source.fromInputStream(inputStream)
      val lines: Iterator[String] = bufferedSource.getLines()

      lines.toList
        .map(line => {
          val id = line.toLong
          rsbNoteFromId(id, book)
        })
    }

    finally {
      inputStream.close()
    }
  }


  /**
    * Given a RsbNote ID, gets the RsbNoteWeb from the web
    */
  def rsbNoteFromId(id: Long, book: Book): RsbNote = {
    val rsbNoteWeb: RsbNoteWeb = rsbNoteWebFromId(id)
    RsbNote(rsbNoteWeb.verseRange(book),
      rsbNoteWeb.id,
      rsbNoteWeb.title,
      rsbNoteWeb.text)
  }


  /**
    * Given a RsbNote ID, gets the RsbNoteWeb from the web
    */
  def rsbNoteWebFromId(id: Long): RsbNoteWeb = {
    val url: String = "https://www.biblegateway.com/exbib/?pub=reformation-study-bible&chunk=" + id
    val json: String = Source.fromURL(url).mkString
    RsbNoteWebJsonParser.fromJson(json)
  }


  /**
    * Given a url like this:
    *   https://www.biblegateway.com/exbib/contents/?osis=Ruth.1.1-Ruth.200.100
    * Finds the RSB note ids
    */
  private def writeNoteIdsToFile(book: Book): Unit = {

    val url = "https://www.biblegateway.com/exbib/contents/?osis=" +
      book.exbibName + ".1.1-" + book.exbibName + ".200.100"
    val ids: Seq[Long] = getNoteIdsFromUrl(url)

    var fileName = "src\\main\\resources\\rsb\\ids\\" + book.oneYearBibleName + "_ids.txt"
    val printWriter: PrintWriter = new PrintWriter(fileName)
    ids.foreach(id => printWriter.println(id))
    printWriter.close()
  }


  /**
    * Given a url like this:
    *   https://www.biblegateway.com/exbib/contents/?osis=Ruth.1.1-Ruth.200.100
    * Finds the RSB note ids
    */
  private def getNoteIdsFromUrl(url: String): Seq[Long] = {
    val json: String = Source.fromURL(url).mkString
    getNoteIdsFromJson(json)
  }


  /**
    * Given JSON from a query like this:
    *   https://www.biblegateway.com/exbib/contents/?osis=Ruth.1.1-Ruth.200.100
    * Finds the RSB note ids
    */
  def getNoteIdsFromJson(json: String): Seq[Long] = {
    val jsObject: JsObject = Json.parse(json).as[JsObject]
    val studyBibleJsArray: JsArray = (jsObject \ "data" \ "studybible").as[JsArray]

    val rsbJsObject: JsObject = studyBibleJsArray.value.filter(jsValue => {
      (jsValue \ "title").as[JsString].value == "Reformation Study Bible"
    })
      .head.as[JsObject]

    val ids: Seq[Long] = (rsbJsObject \ "entries").as[JsArray]
      .value.map(jsValue => {
      (jsValue.as[JsObject] \ "id").as[JsString].value.toLong
    })

    ids
  }


  /**
    * Generates the rsb/ids/book_ids.txt files
    */
  private def generateIdFiles(): Unit = {
    Books.allBooks.foreach(book => {
      println("Starting " + book.oneYearBibleName)
      println("Finished " + book.oneYearBibleName)
      sleep()
    })

    def sleep(): Unit = {
      val tenSec: Long = 10*1000
      val now: Long = new Date().getTime

      while (new Date().getTime < now + tenSec) {
        Thread.sleep(1000)
      }
    }
  }



  /////////////////////////////   main

  def main(args: Array[String]): Unit = {

  }


}

