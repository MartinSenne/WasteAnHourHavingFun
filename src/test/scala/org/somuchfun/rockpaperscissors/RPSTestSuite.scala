package org.somuchfun.rockpaperscissors

import org.scalatest.FunSuite
import org.somuchfun.rockpaperscissors.players.Player
import org.somuchfun.rockpaperscissors.exceptions._



class VariantTestSuite extends FunSuite {

  test("The win function works properly.") {
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


class SequencePlayer(val nameAdd: String, val playerDef: PlayerId, sequence: Seq[Int]) extends Player {

  val name = "SequencePlayer " + nameAdd

  val it = sequence.iterator

  /** Determine next move. */
  override def nextChoice(game: Game): Int = {
    val choice = if (it.hasNext) it.next else 0
    println(choice)
    choice
  }
}

class DummyPlayer(val playerDef: PlayerId) extends Player {
  val name = "DummyPlayer " + playerDef

  /** Determine next move. */
  override def nextChoice(game: Game): Int = 0
}

class FullGameTestSuite extends FunSuite {
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
  
class SubmitChoicesTestSuite extends FunSuite {
  def preparedGame : Game = {
    val playerA = new DummyPlayer(PlayerIdA)
    val playerB = new DummyPlayer(PlayerIdB)

    val game = Game(new RegularVariant, playerA, playerB, 3)

    game.submitMove(PlayerIdA, 1, 1)
    game.submitMove(PlayerIdB, 1, 3)
    
    game
  }
  
  test("Invalid submitted round number is handled correctly.") {
    val game = preparedGame

    val invalidStepResult = game.submitMove(PlayerIdA, 97, 1) // invalid step
    
    assertResult(true)(invalidStepResult.isFailure)
    intercept[IncorrectRoundNumberException] { invalidStepResult.get }
  }

  test("Double commit of choice is rejected.") {
    val game = preparedGame

    game.submitMove(PlayerIdA, 2, 1)
    val doubleMove = game.submitMove(PlayerIdA, 2, 1) // invalid steps

    assertResult(true)(doubleMove.isFailure)
    intercept[MoveAlreadyMadeException] { doubleMove.get }
  }

  test("A finished game does not accept moves.") {
    val game = preparedGame

    game.submitMove(PlayerIdA, 2, 2)
    game.submitMove(PlayerIdB, 2, 1)
    game.submitMove(PlayerIdA, 3, 3)
    game.submitMove(PlayerIdB, 3, 2)
    val doubleMove = game.submitMove(PlayerIdA, 4, 1) // incorrect
    
    assertResult(true)(doubleMove.isFailure)
    intercept[GameAlreadyFinishedException] { doubleMove.get }
  }
}