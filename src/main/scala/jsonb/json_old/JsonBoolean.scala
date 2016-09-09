package jsonb.json_old

/**
  * Boolean Json
  */
case class JsonBoolean(value: Boolean) extends Json {

  override def toJsonString(indentor: Indentor): String = {
    val indent = if (indentor.startOfLine) indentor.toString else ""
    indent + value.toString
  }
}
