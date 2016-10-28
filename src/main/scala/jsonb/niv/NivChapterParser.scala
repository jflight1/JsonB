package jsonb.niv

import jsonb.JsonParserBase
import play.api.libs.json.{JsObject, JsString, JsValue, Json}

import scala.collection.immutable.IndexedSeq


/**
  * json representation:
  * {
  *   "chapterNumber": 11,
  *   "verses": [
  *     "1:Jephthah the Gileadite was a mighty warrior...",
  *     "2:Gilead's wife also bore him sons, and when ...",
  *     "3:So Jephthah fled from his brothers and settled ..."
  *       ...
  *   ]
  * }
  */
case class NivChapterParser(chapterNumber: Int) extends JsonParserBase[NivChapter] {


  override def toJsValue(nivChapter: NivChapter): JsObject = {

    // Convert the verses into json
    val versesJson: Seq[String] = nivChapter.verses.indices
      .map(i => NivVerseParser(i + 1).toString(nivChapter.verses(i)))

    Json.obj(
      "chapterNumber" -> chapterNumber,
      "verses" -> Json.toJson(versesJson))
  }


  override def fromJson(jsValue: JsValue): NivChapter = {

    val versesJson: Seq[String] = (jsValue \ "verses").as[Seq[String]]
    val nivVerses: Seq[NivVerse] = versesJson.indices
      .map(i => NivVerseParser(i + 1).fromString(versesJson(i)))

    NivChapter(nivVerses)
  }
}


