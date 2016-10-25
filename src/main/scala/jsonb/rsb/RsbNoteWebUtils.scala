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
    writeFile(books(16))
    writeFile(books(17))
    writeFile(books(18))


/*
16	2chronicles	822
17	1kings	816
18	1samuel	810
19	2kings	719
20	2samuel	695
21	mark	678
22	joshua	658
23	judges	618
24	1corinthians	437
25	romans	433
26	nehemiah	406
27	revelation	404
28	daniel	357
29	hebrews	303
30	ezra	280
31	2corinthians	257
32	ecclesiastes	222
33	zechariah	211
34	hosea	197
35	esther	167
36	ephesians	155
37	lamentations	154
38	galatians	149
39	amos	146
40	song	117
41	1timothy	113
42	james	108
43	micah	105
44	1peter	105
45	1john	105
46	philippians	104
47	colossians	95
48	1thessalonians	89
49	ruth	85
50	2timothy	83
51	joel	73
52	2peter	61
53	habakkuk	56
54	malachi	55
55	zephaniah	53
56	jonah	48
57	nahum	47
58	2thessalonians	47
59	titus	46
60	haggai	38
61	philemon	25
62	jude	25
63	obadiah	21
64	3john	14
65	2john	13


 */
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
