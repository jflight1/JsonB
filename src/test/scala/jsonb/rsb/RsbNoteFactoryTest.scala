package jsonb.rsb

import java.io.InputStream

import jsonb.Books
import org.apache.commons.io.IOUtils
import org.junit.Assert._
import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner


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


    println(rsbNote)



  }


}


