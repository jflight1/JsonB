package jsonb.json_old

/**
  * Int Json
  */
case class JsonInt(value: Int) extends Json {

  override def toJsonString(indentor: Indentor): String = {
    val indent = if (indentor.startOfLine) indentor.toString else ""
    indent + value.toString
  }
}