package jsonb.json_old

/**
  *
  *
  *
  */
class JsonArray[T <: Json](jsons: List[T]) extends Json {

  override def toJsonString(indentor: Indentor): String =
  "[\n" +
    elemJson(indentor) +
    indentor.toString + "]"


  private def elemJson(indentor: Indentor): String = elemJson(jsons, indentor)

  private def elemJson(jsons: List[T], indentor: Indentor): String =
    jsons match {
      case Nil => ""
      case json :: Nil => json.toJsonString(indentor.more()) + "\n"
      case json :: remainingJsons => json.toJsonString(indentor.more()) + ",\n" +
        elemJson(remainingJsons, indentor)
    }
}
