package jsonb.verseswithnotes

import jsonb.{JsonParserBase, VerseRange, SingleVerse}
import jsonb.niv.NivVerse
import jsonb.rsb.RsbNote
import play.api.libs.json.JsValue


/**
  * Verses and associated notes
  */
case class VersesWithNotes(singleVerses: Seq[SingleVerse], rsbNotes: Seq[RsbNote])


object VersesWithNotesParser extends JsonParserBase[VersesWithNotes] {
  override def toJsValue(t: VersesWithNotes): JsValue = ???

  override def fromJson(jsValue: JsValue): VersesWithNotes = ???
}
