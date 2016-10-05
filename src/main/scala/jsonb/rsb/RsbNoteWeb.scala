package jsonb.rsb

import jsonb.{JsonParserBase, SingleVerse, VerseRange, Book}
import play.api.libs.json.{Json, JsObject}


/**
  * This class represents the json for a Reformation Study Bible note we get from the web,
  * i.e. an url like this: https://www.biblegateway.com/exbib/?pub=reformation-study-bible&chunk=187307
  */
case class RsbNoteWeb(id: Long,
                      title: String,
                      text: String) {

  // They dash they use is different from the one you type so handle both
  val dashRegex = "[–-]"

  /**
    * Parses the title to get a VerseRange.  The book names in a title string are different from the names
    * we have in our Book objects, so we can't use it to find the Book.  Luckily we already know the book when we
    * call this method so we pass it in as an argument.  While a VerseRange could conceiveable span two books,
    * In an RsbNote it never does.
    */
  def verseRange(book: Book): VerseRange = {
    val oneNumberRegex = ".* (\\d+)$".r
    val twoNumberRegex = ".* (\\d+):(\\d+)$".r
    val threeNumberRegex = (".* (\\d+):(\\d+)" + dashRegex + "(\\d+)$").r
    val fourNumberRegex = (".* (\\d+):(\\d+)" + dashRegex + "(\\d+):(\\d+)$").r

    title match {
      case twoNumberRegex(s1, s2) =>
        val chapter = s1.toInt
        val verse = s2.toInt
        val singleVerse: SingleVerse = SingleVerse(book, chapter, verse)
        VerseRange(singleVerse, singleVerse)

      case threeNumberRegex(s1, s2, s3) =>
        val chapter = s1.toInt
        val verse1 = s2.toInt
        val verse2 = s3.toInt
        VerseRange(SingleVerse(book, chapter, verse1),
          SingleVerse(book, chapter, verse2))

      case fourNumberRegex(s1, s2, s3, s4) =>
        val chapter1 = s1.toInt
        val verse1 = s2.toInt
        val chapter2 = s3.toInt
        val verse2 = s4.toInt
        VerseRange(SingleVerse(book, chapter1, verse1),
          SingleVerse(book, chapter2, verse2))

      case oneNumberRegex(s1) =>
        val chapter = s1.toInt
        val verse1 = 1
        val verse2 = book.chapterNumVerses(chapter - 1) // last verse of the chapter
        VerseRange(SingleVerse(book, chapter, verse1), SingleVerse(book, chapter, verse2))

      case _ => throw new Exception("Unparsable title: " + title)
    }

  }
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
    "text" -> rsbNoteWeb.text)


  override def fromJson(jsObject: JsObject): RsbNoteWeb = {
    RsbNoteWeb(
      (jsObject \ "id").as[String].toLong,
      (jsObject \ "title").as[String],
      (jsObject \ "text").as[String])
  }




}
