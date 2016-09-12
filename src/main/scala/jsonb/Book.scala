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
  def nameMatches(s: String): Boolean = s.toLowerCase.equals(name)
}

object Books {
  private val Old: BookType = BookType.OldTestament
  val Genesis = Book("genesis", Old)
  val Exodus = Book("exodus", Old)

  private val New: BookType = BookType.NewTestament
  val Matthew = Book("matthew", New)

  val Psalms = new Book("psalms", BookType.Psalms) {
    override def nameMatches(s: String): Boolean = s.toLowerCase.contains("psalm") // accept psalm or psalms
  }


  val Proverbs = Book("proverbs", BookType.Proverbs)

  val allBooks = List(
    Book("genesis", Old),
    Book("exodus", Old),
    Book("leviticus", Old),
    Book("numbers", Old),
    Book("deuteronomy", Old),
    Book("joshua", Old),
    Book("judges", Old),
    Book("ruth", Old),
    Book("1samuel", Old),
    Book("2samuel", Old),
    Book("1kings", Old),
    Book("2kings", Old),
    Book("1chronicles", Old),
    Book("2chronicles", Old),
    Book("ezra", Old),
    Book("nehemiah", Old),
    Book("esther", Old),
    Book("job", Old),
    Book("ecclesiastes", Old),
    Book("song", Old),
    Book("isaiah", Old),
    Book("jeremiah", Old),
    Book("lamentations", Old),
    Book("ezekiel", Old),
    Book("daniel", Old),
    Book("hosea", Old),
    Book("joel", Old),
    Book("amos", Old),
    Book("obadiah", Old),
    Book("jonah", Old),
    Book("micah", Old),
    Book("nahum", Old),
    Book("habakkuk", Old),
    Book("zephaniah", Old),
    Book("haggai", Old),
    Book("zechariah", Old),
    Book("malachi", Old),
    
    Book("matthew", New),
    Book("mark", New),
    Book("luke", New),
    Book("john", New),
    Book("acts", New),
    Book("romans", New),
    Book("1corinthians", New),
    Book("2corinthians", New),
    Book("galatians", New),
    Book("ephesians", New),
    Book("philippians", New),
    Book("colossians", New),
    Book("1thessalonians", New),
    Book("2thessalonians", New),
    Book("1timothy", New),
    Book("2timothy", New),
    Book("titus", New),
    Book("philemon", New),
    Book("hebrews", New),
    Book("james", New),
    Book("1peter", New),
    Book("2peter", New),
    Book("1john", New),
    Book("2john", New),
    Book("3john", New),
    Book("jude", New),
    Book("revelation", New),
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

