import play.api.libs.json.{JsValue, JsArray, Json}

import scala.collection.mutable.ArrayBuffer




val seq: Seq[Int] = Seq(1,2 ,3, 4)
val jsValue: JsValue = Json.toJson(seq)
println(Json.prettyPrint(jsValue))





/*


    val jsObjects: Seq[JsObject] = seq.map(t => toJsObject(t))
    val jsValue: JsValue = Json.toJson(jsObjects)
    Json.prettyPrint(jsValue)



case class BookWithChapterNumVerses(book: Book, chapterNumVerses: Seq[Int])


object BookWithChapterNumVersesParser extends JsonParserBase[BookWithChapterNumVerses] {

  override def toJsObject(bookWithChapterNumVerses: BookWithChapterNumVerses): JsObject = {
    val book: Book = bookWithChapterNumVerses.book

    val arr: JsArray = Json.arr(bookWithChapterNumVerses.chapterNumVerses)
    Json.obj(
      "oneYearBibleName" -> book.oneYearBibleName,
      "exbibName" -> book.exbibName,
      "nivName" -> book.nivName,
      "numChapters" -> book.numChapters,
      "isOldTestament" -> book.isOldTestament,
      "chapterNumVerses" -> arr
    )
  }

 */