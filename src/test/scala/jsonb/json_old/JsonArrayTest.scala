package jsonb.json_old

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner


@RunWith(classOf[JUnitRunner])
class JsonArrayTest extends FunSuite {



  test("toString primitive int") {

    val jsonArray: JsonArray[JsonInt] = new JsonArray(List(
      new JsonInt(1),
      new JsonInt(2),
      new JsonInt(3)))
    val jsonString: String = jsonArray.toJsonString(IndentorInstance)
    val expected =
      "[\n  1,\n  2,\n  3\n]"
    assertResult(expected)(jsonString)
  }


  test("toString string array") {

    val jsonArray: JsonArray[JsonString] = new JsonArray(List(
      new JsonString("a"),
      new JsonString("b"),
      new JsonString("c")))
    val jsonString: String = jsonArray.toJsonString(IndentorInstance)
    val expected =
      "[\n  \"a\",\n  \"b\",\n  \"c\"\n]"
    assertResult(expected)(jsonString)
  }


  test("toString empty") {

    val jsonString: String = new JsonArray(List())
      .toJsonString(IndentorInstance)

    val expected =
      "[\n" +
        "]"

    assertResult(expected)(jsonString)
  }


  test("toString nested object") {




    val jsonObject: JsonObject = new JsonObject() {
      override def jsonFields = List(
        new JsonField("objectField0", new JsonObject() {
          override def jsonFields = List(
            new JsonField("intField0", new JsonInt(123)),
            new JsonField("booleanField0", new JsonBoolean(true)),
            new JsonField("stringField0", new JsonString("string value")))
        }),
        new JsonField("intField1", new JsonInt(123)),
        new JsonField("booleanField1", new JsonBoolean(true)),
        new JsonField("stringField1", new JsonString("string value")))
    }

    val jsonArray: JsonArray[JsonObject] = new JsonArray(List(jsonObject, jsonObject, jsonObject))


    val jsonString: String = jsonArray.jsonString

    val expected =
      "[\n  {\n    objectField0 = {\n      intField0 = 123,\n      booleanField0 = true,\n      stringField0 = \"string value\"\n    },\n    intField1 = 123,\n    booleanField1 = true,\n    stringField1 = \"string value\"\n  },\n  {\n    objectField0 = {\n      intField0 = 123,\n      booleanField0 = true,\n      stringField0 = \"string value\"\n    },\n    intField1 = 123,\n    booleanField1 = true,\n    stringField1 = \"string value\"\n  },\n  {\n    objectField0 = {\n      intField0 = 123,\n      booleanField0 = true,\n      stringField0 = \"string value\"\n    },\n    intField1 = 123,\n    booleanField1 = true,\n    stringField1 = \"string value\"\n  }\n]"

    assertResult(expected)(jsonString)
  }

}


