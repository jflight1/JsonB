package jsonb.verseswithnotes

import jsonb.{VerseRange, SingleVerse}
import jsonb.niv.NivVerse
import jsonb.rsb.RsbNote


/**
  * A verse and associated notes
  */
case class VerseWithNotes(singleVerse: SingleVerse, nivVerse: NivVerse, rsbNotes: Seq[RsbNote])


object VersesWithNotes {

  /**
    * for each verse, all the RsbNotes for which the verse is first
    */
  def versesWithNotesInRange(verseRange: VerseRange): Seq[(SingleVerse, Seq[RsbNote])] = {

    // the notes with the first verse it applies to
    val notesAndFirstVerse: Seq[(RsbNote, SingleVerse)] = verseRange.rsbNotes
      .map(rsbNote => (rsbNote, verseRange.intersection(rsbNote.verseRange).get.start))

    verseRange.singleVerses
      .map(singleVerse => {
        val rsbNotesForVerse: Seq[RsbNote] = notesAndFirstVerse
          .filter(noteAndFirstVerse => noteAndFirstVerse._2 == singleVerse)
          .map(noteAndFirstVerse => noteAndFirstVerse._1)

        (singleVerse, rsbNotesForVerse)
      })
  }


}
