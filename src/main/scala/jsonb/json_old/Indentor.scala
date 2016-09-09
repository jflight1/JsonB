package jsonb.json_old

/**
  * Manages indentation
  */
class Indentor(indent: Int, val startOfLine: Boolean) {

  if (indent < 0) throw new IllegalArgumentException("indent < 0")

  def more():Indentor = new Indentor(indent + 1, true)

  def sameLine():Indentor = new Indentor(indent, false)

  override def toString: String = toString(indent)


  ///////////////////////////////   private


  private def toString(i: Int): String = i match {
    case 0 => ""
    case _ => "  " + toString(i - 1)
  }
}

object IndentorInstance extends Indentor(0, true) {}

