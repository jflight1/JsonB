package jsonb.html

import java.io.PrintWriter


/**
  * Writes an html file.  Caller provides the content of the <body> element
  */
case class SimpleHtmlFileWriter(path: String, bodyContent: Seq[Element]) {
  def write(): Unit = {

    val html = "<!DOCTYPE html>\n" +
      Element("html", Nil,
        Seq(
          Element("meta", Seq(Attribute("name", "viewport"), Attribute("content", "width=device-width")), Nil),
          Element("body", Nil, bodyContent))
        ).toString(0)

    val outFileName = "src\\web\\html\\" + path

    val printWriter: PrintWriter = new PrintWriter(outFileName)
    printWriter.println(html)
    printWriter.close()

  }

}
