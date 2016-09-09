package jsonb.json_old

/**
  *
  *
  *
  */
case class JsonField(name: String, value: Json)  extends Json {
  override def toJsonString(indentor: Indentor): String = indentor.toString + name + " = " + value.toJsonString(indentor.sameLine())
}

