package jsonb.html




case class Attribute(name:String, value:String) {
  override def toString: String = name + "=\"" + value + "\""
}
