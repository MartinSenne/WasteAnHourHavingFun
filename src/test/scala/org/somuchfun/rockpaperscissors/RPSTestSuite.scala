package org.somuchfun.rockpaperscissors

import org.scalatest.FunSuite
import org.somuchfun.rockpaperscissors.players.Player
import org.somuchfun.rockpaperscissors.exceptions._

class SequencePlayer(val nameAdd: String, val playerDef: PlayerPos, sequence: Seq[Int]) extends Player {

  val name = "SequencePlayer " + nameAdd

  val it = sequence.iterator

  /** Determine next move. */
  override def nextChoice(game: Game): Int = {
    val choice = if (it.hasNext) it.next else 0
    println(choice)
    choice
  }
}

class DummyPlayer(val playerDef: PlayerPos) extends Player {
  val name = "DummyPlayer " + playerDef

  /** Determine next move. */
  override def nextChoice(game: Game): Int = 0
}

class RPSTestSuite extends FunSuite {

  import Main._
  
  test("The win function works properly.") {
    val variant = new RegularVariant

    assertResult( Draw )( variant.beats( 1, 1 ) )   // Rock equals Rocks
    assertResult( PlayerPosB )( variant.beats( 1, 2 ) )      // Rock fails against Paper
    assertResult( PlayerPosA )( variant.beats( 1, 3 ) )      // Rock beats Scissors

    assertResult( PlayerPosA )( variant.beats( 2, 1 ) )      // Paper beats Rocks
    assertResult( Draw )( variant.beats( 2, 2 ) )   // Paper equals Paper
    assertResult( PlayerPosB )( variant.beats( 2, 3 ) )      // Paper fails against Scissors

    assertResult( PlayerPosB )( variant.beats( 3, 1 ) )      // Scissors fails against Rocks
    assertResult( PlayerPosA )( variant.beats( 3, 2 ) )      // Scissors beats Paper
    assertResult( Draw )( variant.beats( 3, 3 ) )   // Scissors equals Scissors
  }
  
  // @Todo: Add general test version instead of fixed version.
  test("Game with no draws ends correctly.") {
    val playerA = new SequencePlayer("A", PlayerPosA, Seq(1,2,3))
    val playerB = new SequencePlayer("B", PlayerPosB, Seq(3,1,2))
    
    val game = new GameImpl(new RegularVariant, playerA, playerB, 3)
    val status = game.play
    
    val comp =  status.completedRounds.toArray
    
    assertResult( 3 ) ( comp.size )
    assertResult( CompletedRound(1, 3, PlayerPosA ) )( comp(0) )
    assertResult( CompletedRound(2, 1, PlayerPosA ) )( comp(1) )
    assertResult( CompletedRound(3, 2, PlayerPosA ) )( comp(2) )
  }

  test("Game with draws ends correctly.") {
    val playerA = new SequencePlayer("A", PlayerPosA, Seq(1,2,3,2,3))
    val playerB = new SequencePlayer("B", PlayerPosB, Seq(3,2,3,1,2))

    val game = new GameImpl(new RegularVariant, playerA, playerB, 3)
    val status = game.play

    val comp =  status.completedRounds.toArray

    assertResult( 5 ) ( comp.size )
    assertResult( CompletedRound(1, 3, PlayerPosA ) )( comp(0) )
    assertResult( CompletedRound(2, 2, Draw ) )( comp(1) )
    assertResult( CompletedRound(3, 3, Draw ) )( comp(2) )
    assertResult( CompletedRound(2, 1, PlayerPosA ) )( comp(3) )
    assertResult( CompletedRound(3, 2, PlayerPosA ) )( comp(4) )
  }
  
  def preparedGame : Game = {
    val playerA = new DummyPlayer(PlayerPosA)
    val playerB = new DummyPlayer(PlayerPosB)

    val game = new GameImpl(new RegularVariant, playerA, playerB, 3)

    game.submitMove(PlayerPosA, 1, 1)
    game.submitMove(PlayerPosB, 1, 3)
    
    game
  }
  
  test("Invalid round number is handled correctly by game.") {
    val game = preparedGame

    val invalidStepResult = game.submitMove(PlayerPosA, 97, 1) // invalid step
    
    assertResult(true)(invalidStepResult.isFailure)
    intercept[IncorrectRoundNumberException] { invalidStepResult.get }
  }

  test("Double commit of choice is rejected.") {
    val game = preparedGame

    game.submitMove(PlayerPosA, 2, 1)
    val doubleMove = game.submitMove(PlayerPosA, 2, 1) // invalid steps

    assertResult(true)(doubleMove.isFailure)
    intercept[MoveAlreadyMadeException] { doubleMove.get }
  }

  test("A finished game does not accept moves.") {
    val game = preparedGame

    game.submitMove(PlayerPosA, 2, 2)
    game.submitMove(PlayerPosB, 2, 1)
    game.submitMove(PlayerPosA, 3, 3)
    game.submitMove(PlayerPosB, 3, 2)
    val doubleMove = game.submitMove(PlayerPosA, 4, 1) // incorrect
    
    assertResult(true)(doubleMove.isFailure)
    intercept[GameAlreadyFinishedException] { doubleMove.get }
  }
}