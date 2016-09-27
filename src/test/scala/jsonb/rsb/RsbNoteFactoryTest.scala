package jsonb.rsb

import java.io.InputStream

import jsonb.{VerseRange, SingleVerse, Books}
import org.apache.commons.io.IOUtils
import org.junit.Assert._
import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import jsonb.Assert._


@RunWith(classOf[JUnitRunner])
class RsbNoteFactoryTest extends FunSuite {

  test("getNoteIdsFromJson") {
    val inputStream: InputStream = getClass.getResourceAsStream("/exbib_osis.json")
    val json: String = IOUtils.toString(inputStream, "UTF-8")

    val ids: Seq[Long] = RsbNoteFactory.getNoteIdsFromJson(json)

    assertEquals(50, ids.size)
    assertEquals(187258, ids(0))
    assertEquals(187307, ids(49))
  }


  test("rsbNoteFromId 188098") {

    val book = Books.find("1kings")
    val rsbNote: RsbNote = RsbNoteFactory.rsbNoteFromId(188098, book)

    assertVerseRangesEqual(
      VerseRange(SingleVerse(book,1,1),SingleVerse(book,2,11)),
      rsbNote.verseRange
    )
    assertEquals(188098, rsbNote.id)
    assertEquals("1 Kings 1:1–2:11", rsbNote.title)
    assertTrue(rsbNote.text.contains("Solomon succeeds David as king in spite"))
  }


  test("rsbNoteFromId 188100") {

    val book = Books.find("1kings")
    val rsbNote: RsbNote = RsbNoteFactory.rsbNoteFromId(188100, book)

    println(rsbNote)

    assertVerseRangesEqual(
      VerseRange(SingleVerse(book,1,3),SingleVerse(book,1,3)),
      rsbNote.verseRange
    )
    assertEquals(188100, rsbNote.id)
    assertEquals("1 Kings 1:3", rsbNote.title)
    assertTrue(rsbNote.text.contains("Shunem was sixteen miles southwest"))

  }

  // todo: test "1 Kings 1:3-5" style


}


