package jsonb.json_old

/**
  * String Json
  */
case class JsonString(value: String) extends Json {

  override def toJsonString(indentor: Indentor): String = {
    val indent = if (indentor.startOfLine) indentor.toString else ""
    indent + "\"" + value.toString + "\""
  }
}
