package org.somuchfun.rockpaperscissors.util

import java.io.ByteArrayInputStream

object StdInUtil {
  def useStringAsStdIn(input: String): Unit = {
    println("a")
    val in = new ByteArrayInputStream(input.getBytes)

    // does not work: System.setIn(in)
    Console.setIn(in)
  }
}
