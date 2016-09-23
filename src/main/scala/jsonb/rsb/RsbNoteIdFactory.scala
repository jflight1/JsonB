package jsonb.rsb

import java.io.PrintWriter
import java.util.Date

import jsonb._
import play.api.libs.json._

import scala.io.Source



object RsbNoteIdFactory {



  /**
    * Given a url like this:
    *   https://www.biblegateway.com/exbib/contents/?osis=Ruth.1.1-Ruth.200.100
    * Finds the RSB note ids
    */
  def writeNoteIdsToFile(bookInfo: BookInfo): Unit = {

    val url = "https://www.biblegateway.com/exbib/contents/?osis=" +
      bookInfo.exbibName + ".1.1-" + bookInfo.exbibName + ".200.100"
    val ids: Seq[Long] = getNoteIdsFromUrl(url)

    var fileName = "src\\main\\resources\\rsb\\ids\\" + bookInfo.oneYearBibleName + "_ids.txt"
    val printWriter: PrintWriter = new PrintWriter(fileName)
    ids.foreach(id => printWriter.println(id))
    printWriter.close()
  }


  /**
    * Given a url like this:
    *   https://www.biblegateway.com/exbib/contents/?osis=Ruth.1.1-Ruth.200.100
    * Finds the RSB note ids
    */
  def getNoteIdsFromUrl(url: String): Seq[Long] = {
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
    BookInfos.allBookInfos.foreach(bookInfo => {
      println("Starting " + bookInfo.oneYearBibleName)
      println("Finished " + bookInfo.oneYearBibleName)
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

