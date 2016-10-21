package jsonb.rsb

import jsonb._


/**
  * This class represents the json for a Reformation Study Bible note.  Our json
  * representation is different from what we get from the .  We represent those
  * with RsbNote.
  */
case class RsbNote(verseRange: VerseRange,
                   id: Long,
                   title: String,
                   text: String)



