package jsonb.rsb

import java.io.{PrintWriter, InputStream}

import jsonb._
import play.api.libs.json.{JsValue, Json, JsObject}

import scala.io.{BufferedSource, Source}


/**
  * Various methods related to getting RsbNotes from the web
  */
object RsbNoteWebUtils {


  /**
    * Get RsbNotes for a whole book
    */
  def writeFile(book: Book): Unit = {
    val rsbNotes: Seq[RsbNote] = rsbNotesFromWeb(book)
    val json: String = RsbNoteJsonParser.seqToJson(rsbNotes)
    val fileName = "src\\main\\resources\\rsb\\notes_json\\" + book.oneYearBibleName + ".json"
    val printWriter: PrintWriter = new PrintWriter(fileName)
    printWriter.println(json)
    printWriter.close()
  }


  /**
    * Get RsbNotes for a whole book
    */
  def rsbNotesFromWeb(book: Book): Seq[RsbNote] = {

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


  def main(args: Array[String]): Unit = {
    val books = Books.allBooksSortedLargestToSmallest

    val start = 55
    val howMany = 11

    for (i <- start until start + howMany) {
      val book = books(i)
      writeFile(book)
      println("Finished " + book.oneYearBibleName)
    }

  }
}


/**
  * This class represents the json for a Reformation Study Bible note we get from the web,
  * i.e. an url like this: https://www.biblegateway.com/exbib/?pub=reformation-study-bible&chunk=187307
  * This is different from our json for an RsbNote
  */
case class RsbNoteWeb(id: Long,
                      title: String,
                      text: String) {

  def verseRange(book: Book): VerseRange = RsbNoteTitleParser.verseRange(title, book)
}



/*
{
  "id": "185570",
  "text": "<div class=\"article\"><p><b><a class=\"bibleref\" href=\"\/passage\/?search=Exod 2:11-Exod 2:15\">2:11\u201315<\/a><\/b> Now forty years old (<a class=\"bibleref\" href=\"\/passage\/?search=Acts 7:23\">Acts 7:23<\/a>), Moses identifies himself with God\u2019s people (<a class=\"bibleref\" href=\"\/passage\/?search=Heb 11:24-Heb 11:27\">Heb. 11:24\u201327<\/a>). His effort to deliver an Israelite from oppression proves vain when he seeks to be a judge of Israel (v. 14).<\/p><\/div>",
  "title": "Ex 2:11\u201315",
  "link_isbn": "",
  "pub_title": "Reformation Study Bible",
  "pub_copyright": "",
  "pub_merch_link": "<a onclick=\"_gaq.push(['_trackEvent', 'CBD Passage', 'click', 'RSB']);\" target=\"_blank\" rel=\"nofollow\" href=\"http:\/\/biblegateway.christianbook.com\/Christian\/Books\/product?item_no=694444&amp;p=1172418\">Buy Reformation Study Bible<\/a>",
  "pub_footnote": "Generously provided by <a target=\"_blank\" href=\"http:\/\/www.ligonier.org\/\">Ligonier Ministries<\/a>. Try their Bible study magazine, Tabletalk, <a target=\"_blank\" rel=\"nofollow\" href=\"https:\/\/subscribe.pcspublink.com\/sub\/subscribeformtblt_trialb.aspx?t=JTRSBG&amp;p=TBLT\">free for 3 months<\/a>.",
  "pub_permalink": "reformation-study-bible",
  "access": "public",
  "pub_sku": "",
  "pub_image_sku": "694400",
  "store_link": ""
}*/

object RsbNoteWebJsonParser extends JsonParserBase[RsbNoteWeb] {


  override def toJsValue(rsbNoteWeb: RsbNoteWeb): JsObject = Json.obj(
    "id" -> rsbNoteWeb.id,
    "title" -> rsbNoteWeb.title,
    "text" -> rsbNoteWeb.text)


  override def fromJson(jsValue: JsValue): RsbNoteWeb = {
    RsbNoteWeb(
      (jsValue \ "id").as[String].toLong,
      (jsValue \ "title").as[String],
      (jsValue \ "text").as[String])
  }

}
