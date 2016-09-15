package jsonb

import play.api.libs.json.{JsObject, Json}


/**
  * Created by jflight on 9/3/2016.
  */
case class VerseRange(start: SingleVerse, end: SingleVerse) extends ToJson {

  def this(jsObject: JsObject) = {
    this(new SingleVerse((jsObject \ "start").as[JsObject]),
      new SingleVerse((jsObject \ "end").as[JsObject]))
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
    val book = Books.fromName(bookName)

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
  def parseMonthFileLine(line: String): List[VerseRange] = {
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
