package jsonb.html

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner


@RunWith(classOf[JUnitRunner])
class HtmlTest extends FunSuite {


  test("html test1") {
    val table = Element("table", Nil, Seq(StringHtmlObject("aaaaaaaaaaaaa")))
    val body = Element("body", Seq(Attribute("a", "b"), Attribute("c", "d")), Seq(table))
    val html = Element("html", Seq(Attribute("a", "b"), Attribute("c", "d")), Seq(body, body, body))
    val s = html.toString
    println(s)
  }


/*
  test("html test2") {
    val element = new Element("html")
    val elements = Elements(Seq(element, element, element))
    val outer = new Element("outer", elements)
    val html = outer.toHtml(Indentor())
    println(html)
  }
*/


}


