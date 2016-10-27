package jsonb.rsb

import java.io.InputStream

import jsonb.Assert._
import jsonb._
import org.apache.commons.io.IOUtils
import org.junit.Assert._
import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner

import scala.io.BufferedSource


@RunWith(classOf[JUnitRunner])
class RsbNoteFactoryTest extends FunSuite {

  test("getNoteIdsFromJson") {
    val json: String = Utils.resourceFileToString("/exbib_osis.json")

    val ids: Seq[Long] = RsbNoteIdUtils.getNoteIdsFromJson(json)

    assertEquals(50, ids.size)
    assertEquals(187258, ids(0))
    assertEquals(187307, ids(49))
  }


  test("rsbNoteFromId 188098 - 1 Kings 1:1–2:11") {

    val book = Books.find("1kings")
    val rsbNote: RsbNote = RsbNoteWebUtils.rsbNoteFromId(188098, book)

    assertVerseRangesEqual(
      VerseRange(SingleVerse(book,1,1),SingleVerse(book,2,11)),
      rsbNote.verseRange
    )
    assertEquals(188098, rsbNote.id)
    assertEquals("1 Kings 1:1–2:11", rsbNote.title)
    assertTrue(rsbNote.text.contains("Solomon succeeds David as king in spite"))
  }


  test("rsbNoteFromId 188100 - 1 Kings 1:3") {

    val book = Books.find("1kings")
    val rsbNote: RsbNote = RsbNoteWebUtils.rsbNoteFromId(188100, book)

    assertVerseRangesEqual(
      VerseRange(SingleVerse(book,1,3),SingleVerse(book,1,3)),
      rsbNote.verseRange
    )
    assertEquals(188100, rsbNote.id)
    assertEquals("1 Kings 1:3", rsbNote.title)
    assertTrue(rsbNote.text.contains("Shunem was sixteen miles southwest"))

  }


  test("rsbNoteFromId 188112 - 1 Kings 1:24–27") {

    val book = Books.find("1kings")
    val rsbNote: RsbNote = RsbNoteWebUtils.rsbNoteFromId(188112, book)

    assertVerseRangesEqual(
      VerseRange(SingleVerse(book,1,24),SingleVerse(book,1,27)),
      rsbNote.verseRange
    )
    assertEquals(188112, rsbNote.id)
    assertEquals("1 Kings 1:24–27", rsbNote.title)
    assertTrue(rsbNote.text.contains("Nathan incisively attacks the issue"))

  }

  test("rsbNotes") {
    val book: Book = Books.find("jude")
    val rsbNotes: Seq[RsbNote] = RsbNoteWebUtils.rsbNotesFromWeb(book)
    val json: String = RsbNoteJsonParser.seqToJson(rsbNotes)
  }


}


