package jsonb

import java.io.InputStream

import org.apache.commons.io.IOUtils



object Utils {

  /**
    * Reads a files from resources and returns the text as a String
    * @param path should start with a "/"
    */
  def resourceFileToString(path: String): String = {
    val inputStream: InputStream = getClass.getResourceAsStream(path)
    try {
      IOUtils.toString(inputStream, "UTF-8")
    }

    finally {
      inputStream.close()
    }
  }


  def paddedString(i: Int): String =
    if (i < 10) "0" + i
    else i.toString

}
