package jsonb

import jsonb.rsb.RsbNote
import jsonb.verseswithnotes.VersesWithNotes
import play.api.libs.json.{Json, JsString, JsValue}



case class VerseRange(start: SingleVerse, end: SingleVerse)
  extends VerseLocation {

  override def toString: String =
    if (start == end)
      start.toString
    else
      start.toString + "-" + end.toString


  /**
    * The single verses for this VerseRange
    */
  lazy val singleVerses: Seq[SingleVerse] = {
    if (start == end)
      Seq(start)

    else {
      start :: VerseRange(start.next, end).singleVerses.toList
    }
  }


  def inRange(singleVerse: SingleVerse): Boolean =
    singleVerse >= this.start && singleVerse <= this.end


  /**
    * Intersection between this VerseRange and another
    */
  def intersection(verseRange: VerseRange): Option[VerseRange] = {
    if (intersects(verseRange)) {
      val start2 = if (start > verseRange.start) start else verseRange.start
      val end2 = if (end < verseRange.end) end else verseRange.end
      Some(VerseRange(start2, end2))
    }

    else None
  }


  def intersects(verseRange: VerseRange): Boolean =
    inRange(verseRange.start) || inRange(verseRange.end) ||
      verseRange.inRange(start) || verseRange.inRange(end)


  lazy val rsbNotes: Seq[RsbNote] = {
    this.books
      // all notes for all books in this VerseRange
      .flatMap(book => book.rsbNotes)
      // only keep the notes that actually intersect with this VerseRange
      .filter(rsbNote => {
      rsbNote.verseRange.intersects(this)
    })
  }


  /**
    * The books in this VerseRange, in order
    */
  lazy val books: Seq[Book] = {
    def booksRec(currentBook: Book, soFar: Seq[Book]): Seq[Book] = {
      val nextSoFar: List[Book] = currentBook :: soFar.toList
      if (currentBook == start.book) nextSoFar
      else booksRec(currentBook.prev, nextSoFar)
    }

    booksRec(end.book, Nil)
  }

  /**
    * For each verse, the RsbNotes for which that verse is the first the note applies to.
    */
  lazy val singleVersesWithNotes: Seq[(SingleVerse, Seq[RsbNote])] = {

    // the notes with the first verse it applies to
    val notesAndFirstVerse: Seq[(RsbNote, SingleVerse)] = this.rsbNotes
      .map(rsbNote => (rsbNote, this.intersection(rsbNote.verseRange).get.start))

    this.singleVerses
      .map(singleVerse => {
        val rsbNotesForVerse: Seq[RsbNote] = notesAndFirstVerse
          .filter(noteAndFirstVerse => noteAndFirstVerse._2 == singleVerse)
          .map(noteAndFirstVerse => noteAndFirstVerse._1)

        (singleVerse, rsbNotesForVerse)
      })
  }


  /**
    * Like versesWithNotes but verses that don't have any notes are grouped with the previous verse that did
    * so we get a Seq of verses with a Seq of RsbNotes
    */
  lazy val versesWithNotes: Seq[VersesWithNotes] =
    VerseRangeVersesWithNotes.get(singleVersesWithNotes)


  lazy val bibleGatewayUrl: String = {
    val urlStart = "https://www.biblegateway.com/passage/?search="
    if (start == end ) {
      urlStart + start.urlEncode
    }
    else if (start.book == end.book) {
      urlStart + start.urlEncode + "-" + end.urlEncode(includeBook = false)
    }
    else {
      urlStart + start.urlEncode + "-" + end.urlEncode
    }
  }


  lazy val displayString: String = {
    if (start == end ) {
      start.book.hghNivName + " " + start.chapter + ":" + start.verse
    }
    else if (start.book == end.book) {
      if (start.chapter == end.chapter) {
        start.book.hghNivName + " " + start.chapter + ":" + start.verse + "-" + end.verse
      }
      else {
        start.book.hghNivName + " " + start.chapter + ":" + start.verse + "-" + end.chapter + ":" + end.verse
      }
    }
    else {
      start.book.hghNivName + " " + start.chapter + ":" + start.verse +
        end.book.hghNivName + " " + end.chapter + ":" + end.verse
    }
  }

  def dayReadingSection: DayReadingSection = {
    DayReadingSection(this, this.bibleGatewayUrl, this.displayString)
  }

}


/**
  * We separate this from compactVersesWithNotes above just for testing
  */
object VerseRangeVersesWithNotes {
  def get(versesWithNotes: Seq[(SingleVerse, Seq[RsbNote])])
  : Seq[VersesWithNotes] = {

    // add an index to the tuples
    val versesWithNotesAndIndex: Seq[(SingleVerse, Seq[RsbNote], Int)] =
      versesWithNotes.indices
        .map(i => (versesWithNotes(i)._1, versesWithNotes(i)._2, i))


    def versesForIndex(i: Int): Seq[SingleVerse] = {

      if (i == versesWithNotesAndIndex.size - 1) Seq(versesWithNotesAndIndex(i)._1)

      else {
        versesWithNotesAndIndex
          // discard tuples before index
          .span(tuple => tuple._3 < i)._2
          // keep tuples after the index until we get to a non-empty one
          .span(tuple => tuple._3 == i || tuple._2.isEmpty)._1
          // just keep the verse
          .map(tuple => tuple._1)
      }
    }

    versesWithNotesAndIndex
      // keep the first and one with non-empty notes
      .filter(tuple => tuple._3 == 0 || tuple._2.nonEmpty)
      .map(tuple => VersesWithNotes(versesForIndex(tuple._3), tuple._2))

  }
}

/**
  * The json will be like
  *   "genesis,1,2"
  *   "genesis,1,2-exodus,3,4"
  * depending on whether start and end differ.
  */
object VerseRangeParser extends JsonParserBase[VerseRange] {


  override def toJsValue(verseRange: VerseRange): JsValue =
    JsString(verseRange.toString)


  override def fromJson(json: String): VerseRange = {
    val jsString: JsString = Json.parse(json).as[JsString]
    fromJson(jsString)
  }


  override def fromJson(jsValue: JsValue): VerseRange = {
    val sVerseRange: String = jsValue.as[JsString].value

    // there'll only be a "-" when start and end differ
    if (sVerseRange.contains("-")) {
      val parts: Array[String] = sVerseRange.split("-")
      val start: SingleVerse = SingleVerseParser.fromString(parts(0))
      val end: SingleVerse = SingleVerseParser.fromString(parts(1))
      VerseRange(start, end)
    }

    else {
      val singleVerse: SingleVerse = SingleVerseParser.fromString(sVerseRange)
      VerseRange(singleVerse, singleVerse)
    }
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





  /////////////////////////////          private


  private def linePartsToVerseRanges(parts: Array[String]): List[VerseRange] = {
    if (parts.isEmpty)
      Nil
    else {
      val firstPart: String = parts.head
      val verseRange = VerseRangeParser.parseText(firstPart)
      verseRange :: linePartsToVerseRanges(parts.tail)
    }
  }


  private def countChar(s: String, c: Char) : Int = s.count(_ == c)


}
