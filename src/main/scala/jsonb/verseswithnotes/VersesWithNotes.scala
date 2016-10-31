package jsonb.verseswithnotes

import jsonb.{VerseRange, SingleVerse}
import jsonb.niv.NivVerse
import jsonb.rsb.RsbNote


/**
  * A verse and associated notes
  */
case class VerseWithNotes(singleVerse: SingleVerse, nivVerse: NivVerse, rsbNotes: Seq[RsbNote])


object VersesWithNotes {

/*
  def versesWithNotes(verseRange: VerseRange): Seq[VerseWithNotes] = {
    verseRange.singleVerses


  }
*/

}
