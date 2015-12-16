package org.somuchfun.rockpaperscissors

import org.scalatest.FunSuite

class RPSTestSuite extends FunSuite {

  import Main._
  
  test("The win function works properly.") {
    val variant = new RegularVariant

    assertResult( Draw )( variant.beats( 1, 1 ) )   // Rock equals Rocks
    assertResult( B )( variant.beats( 1, 2 ) )      // Rock fails against Paper
    assertResult( A )( variant.beats( 1, 3 ) )      // Rock beats Scissors
  }
}