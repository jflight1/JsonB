package jsonb

import java.io.{PrintWriter, FileWriter}
import java.util.Date

import play.api.libs.json._

import scala.io.Source



/**
  * Created by jflight on 9/15/2016.
  */
case class ReformationStudyBibleNote(verseLocations: List[VerseLocation],
                                     id: Long,
                                     title: String,
                                     html: String) {

}



object ReformationStudyBibleNoteFactory {


  /**
    * Given a url like this:
    *   https://www.biblegateway.com/exbib/contents/?osis=Ruth.1.1-Ruth.200.100
    * Finds the RSB note ids
    */
  def writeNoteIdsToFile(bookInfo: BookInfo): Unit = {

    val url = "https://www.biblegateway.com/exbib/contents/?osis=" +
      bookInfo.exbibName + ".1.1-" + bookInfo.exbibName + ".200.100"
    val ids: List[Long] = getNoteIdsFromUrl(url)

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
  def getNoteIdsFromUrl(url: String): List[Long] = {
    val json: String = Source.fromURL(url).mkString
    getNoteIdsFromJson(json)
  }


  /**
    * Given JSON from a query like this:
    *   https://www.biblegateway.com/exbib/contents/?osis=Ruth.1.1-Ruth.200.100
    * Finds the RSB note ids
    */
  def getNoteIdsFromJson(json: String): List[Long] = {
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

    ids.toList

  }


  def main(args: Array[String]): Unit = {

    // everything after exodus
    val bookInfos: List[BookInfo] = BookInfoFactory.allBookInfos.tail.tail

    bookInfos.foreach(bookInfo => {
      println("Starting " + bookInfo.oneYearBibleName)
      ReformationStudyBibleNoteFactory.writeNoteIdsToFile(bookInfo)
      println("Finished " + bookInfo.oneYearBibleName)
      sleep
    })




  }

  def sleep: Unit = {
    val tenSec: Long = 10*1000
    val now: Long = new Date().getTime

    while (new Date().getTime < now + tenSec) {
      Thread.sleep(1000)
    }
  }
}

//