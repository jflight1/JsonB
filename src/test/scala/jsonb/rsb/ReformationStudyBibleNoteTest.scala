package jsonb.rsb

import java.io.InputStream

import org.apache.commons.io.IOUtils
import org.junit.Assert._
import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner


@RunWith(classOf[JUnitRunner])
class ReformationStudyBibleNoteTest extends FunSuite {

  test("getNoteIdsFromJson") {
    val inputStream: InputStream = getClass.getResourceAsStream("/exbib_osis.json")
    val json: String = IOUtils.toString(inputStream, "UTF-8")

    val ids: Seq[Long] = ReformationStudyBibleNoteFactory.getNoteIdsFromJson(json)

    assertEquals(50, ids.size)
    assertEquals(187258, ids(0))
    assertEquals(187307, ids(49))
  }


/*
  test("getNoteIdsFromUrl") {
    val ids: Seq[Long] = ReformationStudyBibleNoteFactory.getNoteIdsFromUrl(
      "https://www.biblegateway.com/exbib/contents/?osis=Ruth.1.1-Ruth.200.100")

    assertEquals(50, ids.size)
    assertEquals(187258, ids(0))
    assertEquals(187307, ids(49))
  }
*/


}


