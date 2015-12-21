package org.somuchfun.rockpaperscissors

import org.scalatest.FunSuite
import org.somuchfun.rockpaperscissors.ui.views.impl.console.util.ConsoleFormatting._

class ConsoleFormattingTestSuite extends FunSuite {
  
  test("Strings should be limited correctly.") {
    assertResult("abcde")(withLength(5,"abcdef"))
    assertResult("ab   ")(withLength(5,"ab"))
    
  }

  test("Score is displayed correctly") {
    assertResult( "1:0")(score(PlayerIdA))
    assertResult( "0:1")(score(PlayerIdB))
    assertResult( "0:0")(score(Draw))
  }
}
