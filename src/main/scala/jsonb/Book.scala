package jsonb

import jsonb.BookType.BookType

/**
  * Created by jflight on 9/3/2016.
  */

object BookType extends Enumeration {
  type BookType = Value
  val OldTestament, NewTestament, Psalms, Proverbs = Value
}

case class Book(name: String, bookType: BookType) {
  def nameMatches(s: String): Boolean = s.toLowerCase.contains(name)
}

object Books {
  private val Old: BookType = BookType.OldTestament
  val Genesis = new Book("genesis", Old)
  val Exodus = new Book("exodus", Old)

  private val New: BookType = BookType.NewTestament
  val Matthew = new Book("matthew", New)

  val Psalms = new Book("psalms", BookType.Psalms) {
    override def nameMatches(s: String): Boolean = s.toLowerCase.contains("psalm") // accept psalm or psalms
  }


  val Proverbs = new Book("proverbs", BookType.Proverbs)

  val allBooks = List(
    Genesis, Exodus,
    Matthew,
    Psalms, Proverbs
  )


  def fromName(name: String): Book = {
    val cleanName: String = name.trim.toLowerCase().replace(" ", "")
    fromName(name, allBooks).get
  }


  private def fromName(name: String, bookList: List[Book]): Option[Book] = {
    if (bookList.isEmpty)
      None

    else if (bookList.head.nameMatches(name))
      Some(bookList.head)

    else fromName(name, bookList.tail)
  }
}

