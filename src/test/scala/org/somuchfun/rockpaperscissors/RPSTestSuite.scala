package org.somuchfun.rockpaperscissors

import org.scalatest.FunSuite
import org.somuchfun.rockpaperscissors.players.Player
import org.somuchfun.rockpaperscissors.exceptions._

class SequencePlayer(val nameAdd: String, val playerDef: PlayerDef, sequence: Seq[Int]) extends Player {

  val name = "SequencePlayer " + nameAdd

  val it = sequence.iterator

  /** Determine next move. */
  override def nextChoice(game: Game): Int = {
    val choice = if (it.hasNext) it.next else 0
    println(choice)
    choice
  }
}

class DummyPlayer(val playerDef: PlayerDef) extends Player {
  val name = "DummyPlayer " + playerDef

  /** Determine next move. */
  override def nextChoice(game: Game): Int = 0
}

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
  
  // @Todo: Add general test version instead of fixed version.
  test("Game with no draws ends correctly.") {
    val playerA = new SequencePlayer("A", A, Seq(1,2,3))
    val playerB = new SequencePlayer("B", B, Seq(3,1,2))
    
    val game = new GameImpl(new RegularVariant, playerA, playerB, 3)
    val status = game.play
    
    val comp =  status.completedRounds.toArray
    
    assertResult( 3 ) ( comp.size )
    assertResult( CompletedRound(1, 3, A ) )( comp(0) )
    assertResult( CompletedRound(2, 1, A ) )( comp(1) )
    assertResult( CompletedRound(3, 2, A ) )( comp(2) )
  }

  test("Game with draws ends correctly.") {
    val playerA = new SequencePlayer("A", A, Seq(1,2,3,2,3))
    val playerB = new SequencePlayer("B", B, Seq(3,2,3,1,2))

    val game = new GameImpl(new RegularVariant, playerA, playerB, 3)
    val status = game.play

    val comp =  status.completedRounds.toArray

    assertResult( 5 ) ( comp.size )
    assertResult( CompletedRound(1, 3, A ) )( comp(0) )
    assertResult( CompletedRound(2, 2, Draw ) )( comp(1) )
    assertResult( CompletedRound(3, 3, Draw ) )( comp(2) )
    assertResult( CompletedRound(2, 1, A ) )( comp(3) )
    assertResult( CompletedRound(3, 2, A ) )( comp(4) )
  }
  
  def preparedGame : Game = {
    val playerA = new DummyPlayer(A)
    val playerB = new DummyPlayer(B)

    val game = new GameImpl(new RegularVariant, playerA, playerB, 3)

    game.submitMove(A, 1, 1)
    game.submitMove(B, 1, 3)
    
    game
  }
  
  test("Invalid round number is handled correctly by game.") {
    val game = preparedGame

    val invalidStepResult = game.submitMove(A, 97, 1) // invalid step
    
    assertResult(true)(invalidStepResult.isFailure)
    intercept[IncorrectRoundNumberException] { invalidStepResult.get }
  }

  test("Double commit of choice is rejected.") {
    val game = preparedGame

    game.submitMove(A, 2, 1)
    val doubleMove = game.submitMove(A, 2, 1) // invalid steps

    assertResult(true)(doubleMove.isFailure)
    intercept[MoveAlreadyMadeException] { doubleMove.get }
  }

  test("A finished game does not accept moves.") {
    val game = preparedGame

    game.submitMove(A, 2, 2)
    game.submitMove(B, 2, 1)
    game.submitMove(A, 3, 3)
    game.submitMove(B, 3, 2)
    val doubleMove = game.submitMove(A, 4, 1) // incorrect
    
    assertResult(true)(doubleMove.isFailure)
    intercept[GameAlreadyFinishedException] { doubleMove.get }
  }
}