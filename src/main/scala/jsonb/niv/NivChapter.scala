package jsonb.niv



case class NivChapter(verses: Seq[NivVerse]) {


  /**
    * one-based verse lookup
    */
  def verse(i: Int) = verses(i - 1)

}


