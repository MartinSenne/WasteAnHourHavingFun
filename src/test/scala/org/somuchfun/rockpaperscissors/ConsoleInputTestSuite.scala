package org.somuchfun.rockpaperscissors

import java.io.ByteArrayInputStream

import org.scalatest.FunSuite
import org.somuchfun.rockpaperscissors.ui.impl.GenericConsoleViews
import org.somuchfun.rockpaperscissors.ui.util.ConsoleInputUtil._

class ConsoleInputTestSuite extends FunSuite {
  
  def useStringAsStdIn(input: String): Unit = {
    println("a")
    val in = new ByteArrayInputStream(input.getBytes)
    
    // does not work: System.setIn(in)
    Console.setIn(in)
  }
  
  test("An user-entered int number must be within range.") {
    useStringAsStdIn("1\r\n")
    assertResult(1)(userNumberSelection("", 1, 3))

    useStringAsStdIn("0\n2\n")
    assertResult(2)(userNumberSelection("", 1, 3))
  }

  test("A user-entered choice must be valid.") {
    useStringAsStdIn("a\r\n")
    assertResult("a")(userAlternativesSelection("Alternatives:", Seq("a","b")))

    useStringAsStdIn("\ne\nb\n")
    assertResult("b")(userAlternativesSelection("Alternatives:", Seq("a","b")))
  }
}
