package jsonb.html


/**
  * Currently content can either be:
  *   - A single StringHtmlObject or
  *   - One or more Elements.
  * i.e. you can't have a mixture of strings and elements.
  */
case class Element(name:String, attributes: Seq[Attribute], content: Seq[HtmlObject])
  extends HtmlObject {

  def this(name:String) {
    this(name, Nil, Nil)
  }


  override def toString: String = toString(0)


  override def toString(indent: Int): String = {
    if (content.isEmpty) {
      "\n" + indentString(indent) +
      "<" + name + attributesToString(attributes) + "/>"
    }

    else if (content.size == 1 && content.head.isInstanceOf[StringHtmlObject]) {
      "\n" + indentString(indent) +
        "<" + name + attributesToString(attributes) + ">" +
        content.head.toString(0) +
        "</" + name + ">"
    }

    else {
      "\n" + indentString(indent) +
        "<" + name + attributesToString(attributes) + ">" +
        contentToString(content, indent + 1) +
        "\n" + indentString(indent) +
        "</" + name + ">"
    }
  }


  ////////////////////////////////    private


  private def attributesToString(attributes: Seq[Attribute]): String = {
    if (attributes.isEmpty) ""

    else {
      " " +
        attributes.head.toString +
        attributesToString(attributes.tail)
    }
  }


  private def contentToString(content: Seq[HtmlObject], indent: Int): String = {
    if (content.isEmpty) ""

    else {
        content.head.toString(indent) +
        contentToString(content.tail, indent)
    }
  }


  private def indentString(indent: Int): String = {
    if (indent == 0) ""
    else "  " + indentString(indent - 1)
  }

}


