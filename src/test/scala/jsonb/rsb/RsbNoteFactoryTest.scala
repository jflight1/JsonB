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


  test("rsbNoteFromId") {

    val rsbNote: RsbNote = RsbNoteFactory.rsbNoteFromId(188098, Books.find("1kings"))

    val book = Books.find("1kings")

    assertVerseRangesEqual(
      VerseRange(SingleVerse(book,1,1),SingleVerse(book,2,11)),
      rsbNote.verseRange
    )


    println(rsbNote)

    //RsbNote(VerseRange(SingleVerse(Book(1kings,1Kgs,22,OldTestament),1,1),SingleVerse(Book(1kings,1Kgs,22,OldTestament),11,11)),188098,1 Kings 1:1–2:11,<div class="article"><p><b><a class="bibleref" href="/passage/?search=1Kgs 1:1-1Kgs 2:11">1:1–2:11</a></b> Solomon succeeds David as king in spite of political turmoil and intrigue by Adonijah, Abiathar, Joab, and Shimei.</p></div>)





  }


}


