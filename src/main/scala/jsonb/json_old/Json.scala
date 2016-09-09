package jsonb.json_old

/**
  * Anything that is json
  */
trait Json {
  def toJsonString(indentor: Indentor): String

  def jsonString: String = toJsonString(IndentorInstance)

}
