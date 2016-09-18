package jsonb.rsb

import java.io.InputStream

import org.apache.commons.io.IOUtils
import org.junit.Assert._
import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner


@RunWith(classOf[JUnitRunner])
class RsbNoteIdFactoryTest extends FunSuite {

  test("getNoteIdsFromJson") {
    val inputStream: InputStream = getClass.getResourceAsStream("/exbib_osis.json")
    val json: String = IOUtils.toString(inputStream, "UTF-8")

    val ids: Seq[Long] = RsbNoteIdFactory.getNoteIdsFromJson(json)

    assertEquals(50, ids.size)
    assertEquals(187258, ids(0))
    assertEquals(187307, ids(49))
  }


}


