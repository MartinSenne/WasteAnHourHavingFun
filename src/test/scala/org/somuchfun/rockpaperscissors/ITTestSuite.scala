package org.somuchfun.rockpaperscissors

import org.scalatest.FunSuite
import org.somuchfun.rockpaperscissors.util.StdInUtil

class ITTestSuite extends FunSuite {
  // @Todo: To be moved to integration test folder, once project gets bigger.
  test("Integration test: A full rock-paper-scissors match with emulated input run through successfully.") {
    val input = "" +
      "c\n" +      // select human vs. human
      "martin\n" + // player name A
      "joe\n" +    // player name B
      "1\n" +      // first round,  A: rock
      "2\n" +      // first round,  B: paper    => 0:1
      "3\n" +      // second round, A: scissors
      "2\n" +      // second round, B: paper    => 1:1
      "2\n" +      // third round,  A: paper
      "2\n" +      // third round,  B: paper    => 1:1
      "1\n" +      // fourth round, A: rock
      "3\n"        // fourth round, B: scissors => 2:1
    
    StdInUtil.useStringAsStdIn(input)
    val control = new GameControl(ConsoleViewFactory)
    val report = control.start
    
    assertResult(4)(report.completedRounds.size)
    assertResult(Score(2,1,PlayerIdA))(report.score)
  }
}
