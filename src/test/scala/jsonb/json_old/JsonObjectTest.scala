package jsonb.json_old

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner


@RunWith(classOf[JUnitRunner])
class JsonObjectTest extends FunSuite {



  test("toString simple fields") {


    val jsonString: String = new JsonObject() {
      override def jsonFields: List[JsonField] = List(
        new JsonField("intField", new JsonInt(123)),
        new JsonField("booleanField", new JsonBoolean(true)),
        new JsonField("stringField", new JsonString("string value")))
    }
      .toJsonString(IndentorInstance)

    val expected =
      "{\n" +
        "  intField = 123,\n" +
        "  booleanField = true,\n" +
        "  stringField = \"string value\"\n" +
        "}"

    assertResult(expected)(jsonString)
  }


  test("toString empty") {

    val jsonString: String = new JsonObject() {
      override def jsonFields: List[JsonField] = List()
    }
      .toJsonString(IndentorInstance)

    val expected =
      "{\n" +
        "}"

    assertResult(expected)(jsonString)
  }


  test("toString nested object") {

    val jsonObject0: JsonObject = new JsonObject() {
      override def jsonFields: List[JsonField] = List(
        new JsonField("intField0", new JsonInt(123)),
        new JsonField("booleanField0", new JsonBoolean(true)),
        new JsonField("stringField0", new JsonString("string value")))
    }

    val jsonObject1: JsonObject = new JsonObject() {
      override def jsonFields: List[JsonField] = List(
        new JsonField("objectField0", jsonObject0),
        new JsonField("intField1", new JsonInt(123)),
        new JsonField("booleanField1", new JsonBoolean(true)),
        new JsonField("stringField1", new JsonString("string value")))
    }

    val jsonString: String = new JsonObject() {
      override def jsonFields: List[JsonField] = List(
        new JsonField("objectField1", jsonObject1),
        new JsonField("intField2", new JsonInt(123)),
        new JsonField("booleanField2", new JsonBoolean(true)),
        new JsonField("stringField2", new JsonString("string value")))
    }
      .toJsonString(IndentorInstance)

    val expected =
      "{\n  objectField1 = {\n    objectField0 = {\n      intField0 = 123,\n      booleanField0 = true,\n      stringField0 = \"string value\"\n    },\n    intField1 = 123,\n    booleanField1 = true,\n    stringField1 = \"string value\"\n  },\n  intField2 = 123,\n  booleanField2 = true,\n  stringField2 = \"string value\"\n}"

    assertResult(expected)(jsonString)
  }



}


