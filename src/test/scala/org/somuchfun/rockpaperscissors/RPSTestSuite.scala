package org.somuchfun.rockpaperscissors

import org.scalatest.FunSuite

class RPSTestSuite extends FunSuite {

  import Main._
  
  test("The win function works properly.") {
    val variant = new RegularVariant

    assertResult( Draw )( variant.beats( 1, 1 ) )   // Rock equals Rocks
    assertResult( B )( variant.beats( 1, 2 ) )      // Rock fails against Paper
    assertResult( A )( variant.beats( 1, 3 ) )      // Rock beats Scissors

    assertResult( A )( variant.beats( 2, 1 ) )      // Paper beats Rocks
    assertResult( Draw )( variant.beats( 2, 2 ) )   // Paper equals Paper
    assertResult( B )( variant.beats( 2, 3 ) )      // Paper fails against Scissors

    assertResult( B )( variant.beats( 3, 1 ) )      // Scissors fails against Rocks
    assertResult( A )( variant.beats( 3, 2 ) )      // Scissors beats Paper
    assertResult( Draw )( variant.beats( 3, 3 ) )   // Scissors equals Scissors
  }
}