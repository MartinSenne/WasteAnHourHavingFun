package org.somuchfun.rockpaperscissors

import org.scalatest.FunSuite
import org.somuchfun.rockpaperscissors.util.StdInUtil
import org.somuchfun.rockpaperscissors.ui.views.impl.console.util.ConsoleInputUtil._

class ConsoleInputTestSuite extends FunSuite {
  
  test("An user-entered int number must be within range.") {
    StdInUtil.useStringAsStdIn("1\r\n")
    assertResult(1)(userNumberSelection("", 1, 3))

    StdInUtil.useStringAsStdIn("0\n2\n")
    assertResult(2)(userNumberSelection("", 1, 3))
  }

  test("A user-entered choice must be valid.") {
    StdInUtil.useStringAsStdIn("a\r\n")
    assertResult("a")(userAlternativesSelection("Alternatives:", Seq("a","b")))

    StdInUtil.useStringAsStdIn("\ne\nb\n")
    assertResult("b")(userAlternativesSelection("Alternatives:", Seq("a","b")))
  }
}
