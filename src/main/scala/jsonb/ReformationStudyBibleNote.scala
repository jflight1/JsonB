package jsonb

import play.api.libs.json._


/**
  * Created by jflight on 9/15/2016.
  */
case class ReformationStudyBibleNote(verseLocations: List[VerseLocation],
                                     id: Long,
                                     title: String,
                                     html: String) {

}



object ReformationStudyBibleNoteFactory {


  /**
    * Given JSON from a query like this:
    *   https://www.biblegateway.com/exbib/contents/?osis=Ruth.1.1-Ruth.100.100
    * Finds the RSB note ids
    */
  def getNoteIds(json: String): List[Long] = {
    val jsObject: JsObject = Json.parse(json).as[JsObject]
    val studyBibleJsArray: JsArray = (jsObject \ "data" \ "studybible").as[JsArray]

    val rsbJsObject: JsObject = studyBibleJsArray.value.filter(jsValue => {
      (jsValue \ "title").as[JsString].value == "Reformation Study Bible"
    })
      .head.as[JsObject]

    val ids: Seq[Long] = (rsbJsObject \ "entries").as[JsArray]
      .value.map(jsValue => {
      (jsValue.as[JsObject] \ "id").as[JsString].value.toLong
    })

    ids.toList

  }
}

//