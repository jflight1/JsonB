package jsonb.niv

import jsonb.Assert._
import jsonb.{Utils, Book, Books}
import org.junit.Assert._
import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import play.api.libs.json.JsString


@RunWith(classOf[JUnitRunner])
class NivTest extends FunSuite {


  test("NivVerseParser") {
    assertEquals("25:aaa", NivVerseParser(25).toString(NivVerse("aaa")))
    assertEquals(JsString("25:aaa"), NivVerseParser(25).toJsValue(NivVerse("aaa")))
    assertEquals(NivVerse("aaa"), NivVerseParser(-1).fromString("25:aaa"))
    assertEquals(NivVerse("aaa"), NivVerseParser(-1).fromJson("\"25:aaa\""))
    assertEquals(NivVerse("aaa"), NivVerseParser(-1).fromJson(JsString("25:aaa")))
  }


  test("NivChapterParser") {
    val nivChapterJson: String = Utils.resourceFileToString("/niv/NivTest_NivChapterParser.json")
    val nivChapter = NivChapter(Seq(
      NivVerse("aaa"),
      NivVerse("bbb"),
      NivVerse("ccc")))

    assertNivChaptersEqual(nivChapter, NivChapterParser(-1).fromJson(nivChapterJson))

  }


  test("HghNivChapterParser") {
    val nivChapterJson: String = Utils.resourceFileToString("/niv/NivTest_HghNivChapterParser.json")
    val nivChapter = NivChapter(Seq(
      NivVerse("aaa"),
      NivVerse("bbb"),
      NivVerse("ccc")))

    assertNivChaptersEqual(nivChapter, HghNivChapterParser.fromJson(nivChapterJson))
  }


  test("NivBookParser") {
    val nivBookJson: String = Utils.resourceFileToString("/niv/NivTest_NivBookParser.json")

    val nivBook = NivBook(Books.find("judges"), Seq(
      NivChapter(Seq(
        NivVerse("aaa1"))),
      NivChapter(Seq(
        NivVerse("aaa2"),
        NivVerse("bbb2"))),
      NivChapter(Seq(
        NivVerse("aaa3"),
        NivVerse("bbb3"),
        NivVerse("ccc3")))
    ))

    assertNivBooksEqual(nivBook, NivBookParser.fromJson(nivBookJson))

    assertEquals(nivBookJson, NivBookParser.toJson(nivBook))
  }


  test("HghNivBookParser") {
    val nivBookJson: String = Utils.resourceFileToString("/niv/NivTest_HghNivBookParser.json")

    val nivBook = NivBook(Books.find("judges"), Seq(
      NivChapter(Seq(
        NivVerse("aaa1"))),
      NivChapter(Seq(
        NivVerse("aaa2"),
        NivVerse("bbb2"))),
      NivChapter(Seq(
        NivVerse("aaa3"),
        NivVerse("bbb3"),
        NivVerse("ccc3")))
    ))

    assertNivBooksEqual(nivBook, HghNivBookParser(Books.find("judges")).fromJson(nivBookJson))

  }



}


