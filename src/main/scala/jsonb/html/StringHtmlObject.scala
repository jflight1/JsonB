package jsonb.html

case class StringHtmlObject(sValue: String) extends HtmlObject {

  override def toString(indent: Int): String = sValue

}
