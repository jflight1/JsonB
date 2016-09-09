package jsonb.json_old

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner


@RunWith(classOf[JUnitRunner])
class IndentorTest extends FunSuite {



  test("toString") {
    assert(IndentorInstance.toString == "")
    assert(IndentorInstance.more().toString == "  ")
    assert(IndentorInstance.more().more().toString == "    ")
  }


/*
  test("negative indent level") {
    try {
      IndentorInstance.less()
      fail("expected exception")
    }
    catch
      {
        case foo: IllegalArgumentException => null
        case t: Throwable => fail("Got: " + t.getClass)
      }
  }
*/

}


