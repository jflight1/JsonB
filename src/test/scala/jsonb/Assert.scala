package jsonb



import org.junit.Assert._




/**
  * Created by jflight on 9/9/2016.
  */
object Assert {


  def assertVerseLocationsEqual(expected: VerseLocation, actual: VerseLocation) = {
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

}

