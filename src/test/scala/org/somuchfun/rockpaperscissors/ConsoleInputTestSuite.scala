package org.somuchfun.rockpaperscissors

import java.io.ByteArrayInputStream

import org.scalatest.FunSuite
import org.somuchfun.rockpaperscissors.ui.ConsoleInput

class ConsoleInputTestSuite extends FunSuite {
  
  def useStringAsStdIn(input: String): Unit = {
    println("a")
    val in = new ByteArrayInputStream(input.getBytes)
    
    // does not work: System.setIn(in)
    Console.setIn(in)
  }
  
  test("An user-entered int number must be within range.") {
    useStringAsStdIn("1\r\n")
    assertResult(1)(ConsoleInput.userNumberSelection("", 1, 3))

    useStringAsStdIn("0\n2\n")
    assertResult(2)(ConsoleInput.userNumberSelection("", 1, 3))
  }

  test("A user-entered choice must be valid.") {
    useStringAsStdIn("a\r\n")
    assertResult("a")(ConsoleInput.userAlternativesSelection("Alternatives:", Seq("a","b")))

    useStringAsStdIn("\ne\nb\n")
    assertResult("b")(ConsoleInput.userAlternativesSelection("Alternatives:", Seq("a","b")))
  }
}