package org.somuchfun.rockpaperscissors

import org.scalatest.FunSuite
import MockPlayers.DummyPlayer
import exceptions._


object PreparedGame {
  def apply() : Game = {
    val playerA = new DummyPlayer(PlayerIdA)
    val playerB = new DummyPlayer(PlayerIdB)

    val game = Game(new RegularVariant, playerA, playerB, 3)

    game.submitMove(PlayerIdA, 1, 1)
    game.submitMove(PlayerIdB, 1, 3)

    game
  }
}

class SubmitChoicesTestSuite extends FunSuite {
  
  test("Invalid submitted round number is handled correctly.") {
    val game = PreparedGame()

    val invalidStepResult = game.submitMove(PlayerIdA, 97, 1) // invalid step
    
    assertResult(true)(invalidStepResult.isFailure)
    intercept[IncorrectRoundNumberException] { invalidStepResult.get }
  }

  test("Double commit of choice is rejected.") {
    val game = PreparedGame()

    game.submitMove(PlayerIdA, 2, 1)
    val doubleMove = game.submitMove(PlayerIdA, 2, 1) // invalid steps

    assertResult(true)(doubleMove.isFailure)
    intercept[MoveAlreadyMadeException] { doubleMove.get }
  }

  test("A finished game does not accept moves.") {
    val game = PreparedGame()

    game.submitMove(PlayerIdA, 2, 2)
    game.submitMove(PlayerIdB, 2, 1)
    game.submitMove(PlayerIdA, 3, 3)
    game.submitMove(PlayerIdB, 3, 2)
    val doubleMove = game.submitMove(PlayerIdA, 4, 1) // incorrect
    
    assertResult(true)(doubleMove.isFailure)
    intercept[GameAlreadyFinishedException] { doubleMove.get }
  }
}
