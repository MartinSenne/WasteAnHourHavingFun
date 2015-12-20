package org.somuchfun.rockpaperscissors.ui.views.impl.console.util

import scala.io.StdIn
import scala.util.Try

object ConsoleInputUtil {

  def userNumberSelection(text: String, min: Int, max : Int): Int = {
    var result: Try[Int] = null
    do {
      result = Try {
        print(text)
        val choice = StdIn.readLine().toInt
        if ((min <= choice) && (choice <= max)) choice else throw new IllegalArgumentException("Input not in range.")
      }
    } while (result.isFailure)

    result.get
  }

  def userAlternativesSelection(text: String, strings: Seq[String]): String = {
    var result: Try[String] = null
    do {
      result = Try {
        print(text)
        val input = StdIn.readLine()
        if (strings.contains(input)) input else throw new IllegalArgumentException("Input not allowed.")
      }
    } while (result.isFailure)

    result.get
  }
}