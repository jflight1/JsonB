package jsonb.niv

import java.io.InputStream

import jsonb.{Books, JsonParserBase}
import play.api.libs.json.{JsObject, Json}

import scala.collection.Set



/**
  * Represents a book in NIV.json
  * Right now all we have is the book name but the plan is to add more
  */
case class NivBook(name: String)


object NivBookParser extends JsonParserBase[NivBook] {

  override def toJsObject(nivBook: NivBook): JsObject = Json.obj(
    "name" -> nivBook.name)

  override def fromJson(jsObject: JsObject): NivBook =
  NivBook((jsObject \ "name").as[String])


  def allBooksFromFile: Seq[NivBook] = {

    val inputStream: InputStream = getClass.getResourceAsStream("/NIV.json")

    val rootJsObject: JsObject = Json.parse(inputStream).as[JsObject]
    val keys: Set[String] = rootJsObject.keys
    keys.map(key => NivBook(key)).toSeq

  }


  def main(args: Array[String]): Unit = {


//    allBooksFromFile.sortBy(nivBook => nivBook.name).foreach(nivBook => println(nivBook.name))

    Books.allBooks.sortBy(book => book.oneYearBibleName).foreach(book => println(book.oneYearBibleName))



  }

}