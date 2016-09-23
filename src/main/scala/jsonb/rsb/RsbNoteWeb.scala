package jsonb.rsb

import java.io.PrintWriter
import java.util.Date

import jsonb._
import play.api.libs.json._

import scala.io.Source



/**
  * This class represents the json for a Reformation Study Bible note we get from the web,
  * i.e. an url like this: https://www.biblegateway.com/exbib/?pub=reformation-study-bible&chunk=187307
  */
case class RsbNoteWeb(id: Long,
                      title: String,
                      html: String) {

  /**
    * Parses the title to get a VerseRange
    */
  def rsbNote(bookInfo: BookInfo): RsbNote = ???


  // 2:11–15 No


  private def verseRange(bookInfo: BookInfo): VerseRange = ???
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


  override def toJsObject(rsbNoteWeb: RsbNoteWeb): JsObject = Json.obj(
    "id" -> rsbNoteWeb.id,
    "title" -> rsbNoteWeb.title,
    "html" -> rsbNoteWeb.html)


  override def fromJson(jsObject: JsObject): RsbNoteWeb = RsbNoteWeb(
    (jsObject \ "id").as[Long],
    (jsObject \ "title").as[String],
    (jsObject \ "html").as[String])



}

