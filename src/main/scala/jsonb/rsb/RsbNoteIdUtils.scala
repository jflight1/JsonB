package jsonb.rsb

import java.io.PrintWriter
import java.util.Date

import jsonb.{Book, Books}
import play.api.libs.json.{JsArray, JsObject, JsString, Json}

import scala.io.Source



/**
  * Stuff for working with rsb note ids
  */
object RsbNoteIdUtils {

  /**
    * Given a url like this:
    *   https://www.biblegateway.com/exbib/contents/?osis=Ruth.1.1-Ruth.200.100
    * Finds the RSB note ids
    */
  private def writeNoteIdsToFile(book: Book): Unit = {

    val url = "https://www.biblegateway.com/exbib/contents/?osis=" +
      book.exbibName + ".1.1-" + book.exbibName + ".200.100"
    val ids: Seq[Long] = getNoteIdsFromUrl(url)

    val fileName = "src\\main\\resources\\rsb\\ids\\" + book.oneYearBibleName + "_ids.txt"
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


}

