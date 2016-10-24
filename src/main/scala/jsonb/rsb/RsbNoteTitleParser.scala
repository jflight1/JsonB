package jsonb.rsb

import jsonb.{SingleVerse, VerseRange, Book}

import scala.util.matching.Regex



object RsbNoteTitleParser {

  /**
    * Parses the title to get a VerseRange.  The book names in a title string are different from the names
    * we have in our Book objects, so we can't use it to find the Book.  Luckily we already know the book when we
    * call this method so we pass it in as an argument.  While a VerseRange could conceiveable span two books,
    * In an RsbNote it never does.
    */
  def verseRange(titleRaw: String, book: Book): VerseRange = {

    try {

      val title = titleRaw.replaceAll("title", "1")

      // They dash they use is different from the one you type so handle both
      val dashRegex = "[–-]"

      def lastVerseOfChapter(chapter: Int): Int = book.chapterNumVerses(chapter - 1)

      // Gen 1:2-3:4
      val fourNumbersRegex = (".* (\\d+):(\\d+)" + dashRegex + "(\\d+):(\\d+)$").r

      // Gen 1-2:3
      val threeNumbersAcrossChaptersRegex = (".* (\\d+)" + dashRegex + "(\\d+):(\\d+)$").r

      // Gen 1:2-3
      val threeNumbersSameChapterRegex = (".* (\\d+):(\\d+)" + dashRegex + "(\\d+)$").r

      // Gen 1-2
      val twoNumbersAcrossChaptersRegex = (".* (\\d+)" + dashRegex + "(\\d+)$").r

      // Gen 1:2
      val twoNumbersSameChapterRegex = ".* (\\d+):(\\d+)$".r

      // Gen 1
      val oneNumberRegex = ".* (\\d+)$".r

      title match {
        case fourNumbersRegex(s1, s2, s3, s4) =>
          val chapter1 = s1.toInt
          val verse1 = s2.toInt
          val chapter2 = s3.toInt
          val verse2 = s4.toInt
          VerseRange(SingleVerse(book, chapter1, verse1),
            SingleVerse(book, chapter2, verse2))

        case threeNumbersAcrossChaptersRegex(s1, s2, s3) =>
          val chapter1 = s1.toInt
          val chapter2 = s2.toInt
          val verse2 = s3.toInt
          VerseRange(SingleVerse(book, chapter1, 1), SingleVerse(book, chapter2, verse2))

        case threeNumbersSameChapterRegex(s1, s2, s3) =>
          val chapter = s1.toInt
          val verse1 = s2.toInt
          val verse2 = s3.toInt
          VerseRange(SingleVerse(book, chapter, verse1), SingleVerse(book, chapter, verse2))

        case twoNumbersAcrossChaptersRegex(s1, s2) =>
          val chapter1 = s1.toInt
          val chapter2 = s2.toInt
          val verse2 = lastVerseOfChapter(chapter2)
          VerseRange(SingleVerse(book, chapter1, 1), SingleVerse(book, chapter2, verse2))

        case twoNumbersSameChapterRegex(s1, s2) =>
          val chapter = s1.toInt
          val verse = s2.toInt
          val singleVerse: SingleVerse = SingleVerse(book, chapter, verse)
          VerseRange(singleVerse, singleVerse)

        case oneNumberRegex(s1) =>
          val chapter = s1.toInt
          val verse1 = 1
          val verse2 = lastVerseOfChapter(chapter)
          VerseRange(SingleVerse(book, chapter, verse1), SingleVerse(book, chapter, verse2))

        case _ => throw new Exception("Unparsable title: " + title)
      }
    }

    catch {
      case e: Exception => throw new Exception("Error parsing: " + titleRaw + ", " + book.oneYearBibleName, e)
    }


  }
}
