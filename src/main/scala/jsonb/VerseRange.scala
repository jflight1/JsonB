package jsonb

import play.api.libs.json.{JsValue, Json}


/**
  * Created by jflight on 9/3/2016.
  */
case class VerseRange(start: SingleVerse, end: SingleVerse)
  extends VerseLocation



object VerseRangeParser extends JsonParserBase[VerseRange] {


  override def toJsValue(verseRange: VerseRange): JsValue = Json.obj(
    "start" -> SingleVerseParser.toJsValue(verseRange.start),
    "end" -> SingleVerseParser.toJsValue(verseRange.end))


  override def fromJson(jsValue: JsValue): VerseRange =
    VerseRange(
      SingleVerseParser.fromJson((jsValue \ "start").as[JsValue]),
      SingleVerseParser.fromJson((jsValue \ "end").as[JsValue]))


  /**
    * Handles these formats:
    *   within one chapter: matthew+11:7-30
    *   spans >1 chapter: genesis+16:1-18:15
    *
    * Does not handle spanning >1 book: genesis+50:1-26;exodus+1:1-2:10
    */
  def parseText(text: String): VerseRange = {

    // do some sanity checking.
    // We expect exactly one '+'
    if (countChar(text, '+') != 1)
      throw new Exception("Bad string: " + text)

    // We expect 0 or 1 dashes '-'
    val dashCount: Int = countChar(text, '-')
    if (dashCount != 0 && dashCount != 1)
      throw new Exception("Bad string: " + text)

    // We expect 1 or 2 colons ':'
    val colonCount: Int = countChar(text, ':')
    if (colonCount != 1 && colonCount != 2)
      throw new Exception("Bad string: " + text)

    // find the book
    val plusIndex = text.indexOf("+")
    val bookName = text.substring(0, plusIndex)
    val book = Books.find(bookName)

    val numberText = text.substring(plusIndex + 1)

    if (numberText.indexOf('-') == -1) { // no dash: 11:7
      val colonIndex = numberText.indexOf(':')
      val chapter: Int = numberText.substring(0, colonIndex).toInt
      val verse: Int = numberText.substring(colonIndex + 1).toInt
      VerseRange(SingleVerse(book, chapter, verse), SingleVerse(book, chapter, verse))
    }

    else if (colonCount == 1) { // 11:7-30
      val colonIndex = numberText.indexOf(':')
      val dashIndex = numberText.indexOf('-')
      val chapter: Int = numberText.substring(0, colonIndex).toInt
      val verse1: Int = numberText.substring(colonIndex + 1, dashIndex).toInt
      val verse2: Int = numberText.substring(dashIndex + 1).toInt
      VerseRange(SingleVerse(book, chapter, verse1), SingleVerse(book, chapter, verse2))
    }

    else { // 16:1-18:15
      val verseTexts: Array[String] = numberText.split("-")

      def parse (verseText: String):SingleVerse = {
        val colonIndex = verseText.indexOf(':')
        val chapter = verseText.substring(0, colonIndex).toInt
        val verse = verseText.substring(colonIndex + 1).toInt
        SingleVerse(book, chapter, verse)
      }

      VerseRange(parse(verseTexts(0)), parse(verseTexts(1)))
    }
  }


  /**
    * Parse a line like this:
    *   genesis+39:1-41:16;matthew+12:46-13:23;psalm+17:1-15;proverbs+3:33-35
    */
  def parseMonthTextFileLine(line: String): List[VerseRange] = {
    // each part is either a VerseRange or SingleVerse.
    // VerseRange is just two SingleVerses, so we can turn the parts into a
    // List[SingleVerse]
    val parts: Array[String] = line.split(";")
    linePartsToVerseRanges(parts)
  }


  private def linePartsToVerseRanges(parts: Array[String]): List[VerseRange] = {
    if (parts.isEmpty)
      Nil
    else {
      val firstPart: String = parts.head
      val verseRange = VerseRangeParser.parseText(firstPart)
      verseRange :: linePartsToVerseRanges(parts.tail)
    }
  }


  def countChar(s: String, c: Char) : Int = s.count(_ == c)


}
