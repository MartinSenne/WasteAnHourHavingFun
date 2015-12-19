package org.somuchfun.rockpaperscissors;

import org.scalatest.FunSuite;

class VariantTestSuite extends FunSuite {

  test("The win function for regular variant works properly.") {
    val variant = new RegularVariant

    assertResult(Draw)(variant.beats(1, 1))      // Rock equals Rocks
    assertResult(PlayerIdB)(variant.beats(1, 2)) // Rock fails against Paper
    assertResult(PlayerIdA)(variant.beats(1, 3)) // Rock beats Scissors

    assertResult(PlayerIdA)(variant.beats(2, 1)) // Paper beats Rocks
    assertResult(Draw)(variant.beats(2, 2))      // Paper equals Paper
    assertResult(PlayerIdB)(variant.beats(2, 3)) // Paper fails against Scissors

    assertResult(PlayerIdB)(variant.beats(3, 1)) // Scissors fails against Rocks
    assertResult(PlayerIdA)(variant.beats(3, 2)) // Scissors beats Paper
    assertResult(Draw)(variant.beats(3, 3))      // Scissors equals Scissors
  }
}