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

}

