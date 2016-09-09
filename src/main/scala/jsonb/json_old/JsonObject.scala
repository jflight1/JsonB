package jsonb.json_old



/**
  *
  *
  *
  */
abstract class JsonObject() extends Json {

  override def toJsonString(indentor: Indentor): String = {
    val indent = if (indentor.startOfLine) indentor.toString else ""
    indent + "{\n" +
      fieldJson(indentor) +
      indentor.toString + "}"
  }


  private def fieldJson(indentor: Indentor): String = fieldJson(jsonFields, indentor)

  private def fieldJson(jsonFields: List[JsonField], indentor: Indentor): String =
    jsonFields match {
      case Nil => ""
      case jsonField :: Nil => jsonField.toJsonString(indentor.more()) + "\n"
      case jsonField :: remainingJsonFields => jsonField.toJsonString(indentor.more()) + ",\n" +
        fieldJson(remainingJsonFields, indentor)
    }


  def jsonFields: List[JsonField]
}
