package jsonb

import play.api.libs.json.{JsObject, Json}


/**
  * Created by jflight on 9/3/2016.
  */
case class VerseRange(start: VerseLocation, end: VerseLocation) extends ToJson {

  def this(jsObject: JsObject) = {
    this(new VerseLocation((jsObject \ "start").as[JsObject]),
      new VerseLocation((jsObject \ "end").as[JsObject]))
  }


  override def toJsObject: JsObject = Json.obj(
    "start" -> start.toJsObject,
    "end" -> end.toJsObject)


  override def toJson: String = Json.prettyPrint(toJsObject)

}



object VerseRangeParser extends JsonParser[VerseRange] {


  override def parse(jsObject: JsObject): VerseRange = new VerseRange(jsObject)

  override def parse(json: String): VerseRange = {
    val jsObject: JsObject = Json.parse(json).as[JsObject]
    new VerseRange(jsObject)
  }


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

    // We expect exactly one '-'
    if (countChar(text, '-') != 1)
      throw new Exception("Bad string: " + text)

    // We expect 1 or 2 colons ':'
    val colonCount: Int = countChar(text, ':')
    if (colonCount != 1 && colonCount != 2)
      throw new Exception("Bad string: " + text)

    // find the book
    val plusIndex = text.indexOf("+")
    val bookName = text.substring(0, plusIndex)
    val book = Books.fromName(bookName)

    val numberText = text.substring(plusIndex + 1)
    if (colonCount == 1) { // 11:7-30
    val colonIndex = numberText.indexOf(':')
      val dashIndex = numberText.indexOf('-')
      val chapter: Int = numberText.substring(0, colonIndex).toInt
      val verse1: Int = numberText.substring(colonIndex + 1, dashIndex).toInt
      val verse2: Int = numberText.substring(dashIndex + 1).toInt

      VerseRange(VerseLocation(book, chapter, verse1), VerseLocation(book, chapter, verse2))
    }

    else { // 16:1-18:15
    val verseTexts: Array[String] = numberText.split("-")

      def parse (verseText: String):VerseLocation = {
        val colonIndex = verseText.indexOf(':')
        val chapter = verseText.substring(0, colonIndex).toInt
        val verse = verseText.substring(colonIndex + 1).toInt
        VerseLocation(book, chapter, verse)
      }

      VerseRange(parse(verseTexts(0)), parse(verseTexts(1)))
    }
  }


  /**
    * Parse a line like this:
    *   genesis+39:1-41:16;matthew+12:46-13:23;psalm+17:1-15;proverbs+3:33-35
    */
  def parseMonthFileLine(line: String): List[VerseRange] = {
    // each part is either a VerseRange or VerseLocation.
    // VerseRange is just two VerseLocations, so we can turn the parts into a
    // List[VerseLocation]
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
