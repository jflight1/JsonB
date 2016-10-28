package jsonb


import jsonb.niv.{NivBook, NivChapter, NivVerse}
import org.junit.Assert._




/**
  * Created by jflight on 9/9/2016.
  */
object Assert {


  def assertSingleVersesEqual(expected: SingleVerse, actual: SingleVerse) = {
    assertEquals(expected.book, actual.book)
    assertEquals(expected.chapter, actual.chapter)
    assertEquals(expected.verse, actual.verse)
  }


  def assertVerseRangesEqual(expected: VerseRange, actual: VerseRange) = {
    assertEquals(expected.start, actual.start)
    assertEquals(expected.end, actual.end)
  }

  def assertDayReadingsEqual(expected: DayReading, actual: DayReading) = {
    assertEquals(expected.month, actual.month)
    assertEquals(expected.day, actual.day)
    assertVerseRangesEqual(expected.oldTestament, actual.oldTestament)
    assertVerseRangesEqual(expected.newTestament, actual.newTestament)
    assertVerseRangesEqual(expected.psalms, actual.psalms)
    assertVerseRangesEqual(expected.proverbs, actual.proverbs)
  }


  def assertBooksEqual(expected: Book, actual: Book) = {
    assertEquals(expected.oneYearBibleName, actual.oneYearBibleName)
    assertEquals(expected.exbibName, actual.exbibName)
    assertEquals(expected.hghNivName, actual.hghNivName)
    assertEquals(expected.isOldTestament, actual.isOldTestament)

    assertEquals(expected.chapterNumVerses.size, actual.chapterNumVerses.size)
    for (i <- expected.chapterNumVerses.indices) {
      assertEquals(expected.chapterNumVerses(i), actual.chapterNumVerses(i))
    }
  }


  def assertNivVersesEqual(expected: NivVerse, actual: NivVerse) = {
    assertEquals(expected.text, actual.text)
  }


  def assertNivChaptersEqual(expected: NivChapter, actual: NivChapter) = {
    assertEquals(expected.verses.size, actual.verses.size)
    expected.verses.indices.foreach(i => {
      assertNivVersesEqual(expected.verses(i), actual.verses(i))
    })
  }


  def assertNivBooksEqual(expected: NivBook, actual: NivBook) = {
    assertBooksEqual(expected.book, actual.book)
    assertEquals(expected.chapters.size, actual.chapters.size)
    expected.chapters.indices.foreach(i => {
      assertNivChaptersEqual(expected.chapters(i), actual.chapters(i))
    })
  }


}

