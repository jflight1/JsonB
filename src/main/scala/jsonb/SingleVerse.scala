package jsonb

import jsonb.niv.NivVerse
import play.api.libs.json._



case class SingleVerse(book: Book, chapter: Int, verse: Int)
  extends VerseLocation {

  override def toString: String = book.codeName + "," + chapter + "," + verse


  /**
    * The next verse after this one
 *
    * @throws Exception if you call it with Revelation 22:21 (the last verse)
    */
  lazy val next: SingleVerse = {
    val versesInChapter = book.chapterNumVerses(chapter - 1)
    if (verse < versesInChapter) {
      SingleVerse(book, chapter, verse + 1)
    }

    else if (chapter < book.numChapters) {
      SingleVerse(book, chapter + 1, 1)
    }

    else {
      SingleVerse(book.next, 1, 1)
    }
  }


  lazy val nivVerse: NivVerse = {
    book.nivBook.chapters(chapter - 1).verses(verse - 1)
  }


  def <(that: SingleVerse) =
    this.book < that.book ||
      (this.book == that.book && this.chapter < that.chapter) ||
      (this.book == that.book && this.chapter == that.chapter && this.verse < that.verse)

  def <=(that: SingleVerse) = this < that || this == that

  def >(that: SingleVerse) = !(this <= that)

  def >=(that: SingleVerse) = !(this < that)


}


object SingleVerseParser extends JsonParserBase[SingleVerse] {


  override def toJsValue(singleVerse: SingleVerse): JsValue =
    JsString(singleVerse.toString)


  /**
    * Note: This differs from fromString below because a json string will actually
    * include quotes as part of the string like: "genesis,1,2"
    */
  override def fromJson(json: String): SingleVerse = {
    val jsString: JsString = Json.parse(json).as[JsString]
    fromJson(jsString)
  }


  override def fromJson(jsValue: JsValue): SingleVerse =
    fromString(jsValue.as[JsString].value)


  def fromString(s: String): SingleVerse = {
    val parts: Array[String] = s.split(",")

    SingleVerse(
      book = Books.find(parts(0)),
      chapter = parts(1).toInt,
      verse = parts(2).toInt)
  }

}



