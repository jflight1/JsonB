package jsonb.niv

import jsonb.JsonParserBase
import play.api.libs.json.{JsString, JsObject, JsValue, Json}


/**
  * Parse a chapter from hgh_niv.json
  * json representation.  Note verses aren't in order:
  * {
  *   "24": "Will you not take what your god Chemosh ...",
  *   "25": "Are you better than Balak son of Zippor, ...",
  *   "26": "For three hundred years Israel occupied ..."
  *   ...
  * }
  */
object HghNivChapterParser extends JsonParserBase[NivChapter] {


  /**
    * We never generate HGH json
    */
  override def toJsValue(nivChapter: NivChapter): JsObject = throw new UnsupportedOperationException


  override def fromJson(jsValue: JsValue): NivChapter = {


    val jsObject: JsObject = jsValue.as[JsObject]

    val nivVerses: Seq[NivVerse] = jsObject.keys
      .map(sKey => new IndexAndVerse(jsObject, sKey)) // map key Strings to IndexAndVerses
      .toSeq.sortBy(indexAndVerse => indexAndVerse.index) // sort by index
      .map(indexAndVerse => indexAndVerse.nivVerse) // map to NivVerses

    NivChapter(nivVerses)
  }

  private class IndexAndVerse(val index: Int, val nivVerse: NivVerse) {
    def this(jsObject: JsObject, sKey: String) {
      this(sKey.toInt, NivVerse((jsObject \ sKey).as[JsString].value))
    }
  }

}
