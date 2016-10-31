package jsonb

import org.junit.Assert._
import jsonb.Books._
import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner


@RunWith(classOf[JUnitRunner])
class SingleVerseTest extends FunSuite {

  test("toJson") {
    val jsonString: String = SingleVerseParser.toJson(SingleVerse(genesis, 3, 4))
    assertEquals("\"genesis,3,4\"", jsonString)
  }


  test("parse") {
    val jsonString: String = "\"genesis,1,2\""

    val singleVerse: SingleVerse = SingleVerseParser.fromJson(jsonString)

    assert(singleVerse.book == Books.find("Genesis"))
    assert(singleVerse.chapter == 1)
    assert(singleVerse.verse == 2)
  }


  test("next") {
    assertEquals(SingleVerse(genesis, 1, 1).next, SingleVerse(genesis, 1, 2))
    assertEquals(SingleVerse(genesis, 1, 31).next, SingleVerse(genesis, 2, 1))
    assertEquals(SingleVerse(genesis, 50, 26).next, SingleVerse(exodus, 1, 1))
  }


  test("inequality") {
    assertTrue(SingleVerse(genesis, 2, 1) < SingleVerse(exodus, 1, 1))
    assertTrue(SingleVerse(genesis, 2, 5) < SingleVerse(genesis, 3, 4))
    assertTrue(SingleVerse(genesis, 2, 5) < SingleVerse(genesis, 2, 6))

    assertTrue(SingleVerse(genesis, 2, 1) <= SingleVerse(exodus, 1, 1))
    assertTrue(SingleVerse(genesis, 2, 5) <= SingleVerse(genesis, 3, 4))
    assertTrue(SingleVerse(genesis, 2, 5) <= SingleVerse(genesis, 2, 6))
    assertTrue(SingleVerse(genesis, 2, 5) <= SingleVerse(genesis, 2, 5))

    assertTrue(SingleVerse(exodus, 1, 1) > SingleVerse(genesis, 2, 1))
    assertTrue(SingleVerse(genesis, 3, 4) > SingleVerse(genesis, 2, 5))
    assertTrue(SingleVerse(genesis, 2, 6) > SingleVerse(genesis, 2, 5))

    assertTrue(SingleVerse(exodus, 1, 1) >= SingleVerse(genesis, 2, 1))
    assertTrue(SingleVerse(genesis, 3, 4) >= SingleVerse(genesis, 2, 5))
    assertTrue(SingleVerse(genesis, 2, 6) >= SingleVerse(genesis, 2, 5))
    assertTrue(SingleVerse(genesis, 2, 5) >= SingleVerse(genesis, 2, 5))

  }

}


