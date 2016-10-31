package jsonb.niv

import jsonb.Book

case class NivBook(book: Book, chapters: Seq[NivChapter]) {

  /**
    * one-based chapter lookup
    */
  def chapter(i: Int) = chapters(i - 1)
}



