package jsonb.json_old

/**
  * Boolean Json
  */
case class JsonNull(value: Boolean) extends Json {

  override def toJsonString(indentor: Indentor): String = "null"
}
