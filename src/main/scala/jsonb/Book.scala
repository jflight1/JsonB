package jsonb

import jsonb.BookType.BookType

/**
  * Created by jflight on 9/3/2016.
  */

object BookType extends Enumeration {
  type BookType = Value
  val OldTestament, NewTestament, Psalms, Proverbs = Value
}

case class Book(name: String, bookType: BookType) {}

object Books {
  private val Old: BookType = BookType.OldTestament
  val Genesis = new Book("genesis", Old)
  val Exodus = new Book("exodus", Old)

  private val New: BookType = BookType.NewTestament
  val Matthew = new Book("matthew", New)

  val Psalms = new Book("psalms", BookType.Psalms)
  val Proverbs = new Book("proverbs", BookType.Proverbs)


  def fromName(name: String): Book = {
    val cleanName: String = name.trim.toLowerCase().replace(" ", "")

    cleanName match {
      case "genesis" => Genesis
      case "exodus" => Exodus
      case "matthew" => Matthew
    }
  }
}

