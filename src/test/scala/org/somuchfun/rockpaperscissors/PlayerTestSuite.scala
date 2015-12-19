package org.somuchfun.rockpaperscissors

import org.scalatest.FunSuite
import org.somuchfun.rockpaperscissors.players.ComputerPlayer

class PlayerTestSuite extends FunSuite {
  test("A computer player makes valid moves.") {
    val game = fixture.PreparedGame()
    val computerPlayer = new ComputerPlayer("", PlayerIdA)

    assert{ val r = computerPlayer.nextChoice(game); ((1 <= r) && (r <= game.variant.n)) }
    assert{ val r = computerPlayer.nextChoice(game); ((1 <= r) && (r <= game.variant.n)) }
    assert{ val r = computerPlayer.nextChoice(game); ((1 <= r) && (r <= game.variant.n)) }
  }
}
