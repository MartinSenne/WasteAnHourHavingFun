package org.somuchfun.rockpaperscissors

import org.scalatest.FunSuite
import MockPlayers.SequencePlayer

class GeneralGamePlayTestSuite extends FunSuite {
  // @Todo: Add general test version instead of fixed version.
  test("Game with no draws ends correctly.") {
    val playerA = new SequencePlayer("A", PlayerIdA, Seq(1, 2, 3))
    val playerB = new SequencePlayer("B", PlayerIdB, Seq(3, 1, 2))

    val game = Game(new RegularVariant, playerA, playerB, 3)
    val status = GameSteering.play(game)

    val comp = status.completedRounds.toArray

    assertResult(3)(comp.size)
    assertResult(CompletedRound(1, 3, PlayerIdA))(comp(0))
    assertResult(CompletedRound(2, 1, PlayerIdA))(comp(1))
    assertResult(CompletedRound(3, 2, PlayerIdA))(comp(2))
  }

  test("Game with draws ends correctly.") {
    val playerA = new SequencePlayer("A", PlayerIdA, Seq(1, 2, 3, 2, 3))
    val playerB = new SequencePlayer("B", PlayerIdB, Seq(3, 2, 3, 1, 2))

    val game = Game(new RegularVariant, playerA, playerB, 3)
    val status = GameSteering.play(game)

    val comp = status.completedRounds.toArray

    assertResult(5)(comp.size)
    assertResult(CompletedRound(1, 3, PlayerIdA))(comp(0))
    assertResult(CompletedRound(2, 2, Draw))(comp(1))
    assertResult(CompletedRound(3, 3, Draw))(comp(2))
    assertResult(CompletedRound(2, 1, PlayerIdA))(comp(3))
    assertResult(CompletedRound(3, 2, PlayerIdA))(comp(4))
  }
}
  
