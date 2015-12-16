package org.somuchfun.rockpaperscissors

import org.somuchfun.rockpaperscissors.players._
import org.somuchfun.rockpaperscissors.ui.ConsoleUI

import scala.util.{Failure, Success, Try}


sealed trait PlayerDef
sealed trait RoundResult
case object A extends PlayerDef with RoundResult
case object B extends PlayerDef with RoundResult
case object Draw extends RoundResult

/** Current status of a game. Includes number of round to be played and completed rounds. */
case class GameStatus(playerA: Player, playerB: Player, currentStep : Option[Int], completedRounds: Seq[CompletedRound] )

case class CompletedRound(a: Int, b:Int, result: RoundResult)

/**
  * Game API.
  */
trait Game {
  /**
    * Perform a game move by player `playerDef`.
    * @param playerDef the player
    * @param step the game round we are in
    * @param choice player's choice
    * @return updates [[GameStatus]]
    */
  def submitMove(playerDef: PlayerDef, step: Int, choice: Int) : Try[GameStatus]
  
  /** A variant: Can be Rock-Scissors-Stone or any modulo-based game. */
  def variant: GameVariant
  
  /** Retrieve status of current game. */
  def gameStatus: GameStatus
}

/**
  * Concrete implementation of a [[Game]]
  * @param variant specifies the variant to use.
  * @param playerA information on player A.
  * @param playerB information on player B.
  * @param rounds number of rounds to play.
  */
class GameImpl(val variant: GameVariant, val playerA: Player, val playerB: Player, val rounds: Int) extends Game {
  
  // API impl =================================================
  
  override def gameStatus : GameStatus = GameStatus(playerA, playerB, state.currentStep, state.completedRounds)

  override def submitMove(playerDef: PlayerDef, step: Int, choice: Int) : Try[GameStatus] = {
    // Game is running? None indicates game is over
    if (state.currentStep.isDefined) {
      if (step == state.currentStep.get) {
        if ( state.currentRound.movePossible(playerDef) ) {
          updateState(playerDef, step, choice)
          Success( gameStatus )
        } else {
          Failure( new IllegalArgumentException("Move already made."))
        }
      } else {
        Failure(new IllegalArgumentException("Submitting move that does not have the correct step number."))
      }
    } else {
      Failure(new IllegalArgumentException("Game is already over."))
    }
  }

  def play: Unit = {
    val ui = new ConsoleUI(this)
    ui.view(gameStatus)
    while (state.currentStep.isDefined) {
      playerA.triggerMove(this)
      playerB.triggerMove(this)
      ui.view(gameStatus)
    }
  }
  
  // Internal =====================================================

  /**
    * Case class representing the current state.
    * @param currentStep is the round number that is actually played
    * @param currentRound collects moves of player A and B.
    * @param completedRounds is a list of rounds that have been completed.
    */
  case class State(currentStep : Option[Int], currentRound: Round, completedRounds: Seq[CompletedRound])

  // 
  /**
    * State of the game.
    * 
    * As we are not allowed to use any Framework, we use a central place to keep our state.
    * REMARK: Basically, we should use an Actor of the Akka framework to encapsulate that state.
    * 
    * Initial state is set to round 1, where no choices have been made, and no completed rounds are present so far.
    */
  private var state = State(Some(1), Round(None, None), List())

  case class Round(choiceA: Option[Int], choiceB: Option[Int] ) {
    def completed: Boolean = choiceA.isDefined && choiceB.isDefined
    def movePossible( playerDef: PlayerDef ) : Boolean = playerDef match {
      case A ⇒ choiceA.isEmpty
      case B ⇒ choiceB.isEmpty
    }
    def move( playerDef: PlayerDef, choice: Int ) : Round = playerDef match {
      case A ⇒ this.copy(choiceA = Some(choice))
      case B ⇒ this.copy(choiceB = Some(choice))
    }
    def asCompletedRound : CompletedRound = {
      val a = choiceA.get
      val b = choiceB.get
      CompletedRound(a, b, variant.beats(a,b))
    }
  }

  private def updateState(playerDef: PlayerDef, step: Int, choice: Int) : Unit = {
    val updatedRound = state.currentRound.move(playerDef, choice)
    val newState = if (updatedRound.completed) {
      val newRound = Round(None, None)
      // warning: this add operation is costly ( O(n) ) !!!
      val newCompletedMoves = state.completedRounds :+ updatedRound.asCompletedRound
      val newStep = if (moreRoundsToPlay(newCompletedMoves)) {
         Some(state.currentStep.get + 1)
      } else {
         None // indicates that match is over
      }
      State( newStep, newRound, newCompletedMoves)
      
    } else { // just update the round
      state.copy(currentRound = updatedRound)
    }
    state = newState
  }

  private def moreRoundsToPlay(completedMoves: Seq[CompletedRound]) : Boolean = completedMoves.filter(round => round.result != Draw ).size < rounds  
 
}

object Main {
  def main(args: Array[String]) : Unit = {
    val gameType = ConsoleUI.selectGameType()
    
    val players = gameType match {
      case "a" ⇒ (new ComputerPlayer("Darwin", A), new ComputerPlayer("Watson", B))
      case "b" ⇒ {
        val nameA = ConsoleUI.queryPlayerName( A )
        (new HumanPlayer(nameA, A), new ComputerPlayer("Watson", B))
      }
      case "c" ⇒ {
        val nameA = ConsoleUI.queryPlayerName( A )
        val nameB = ConsoleUI.queryPlayerName( B )
        (new HumanPlayer(nameA, A), new HumanPlayer(nameB, B))
      }
    }
    
    val game = new GameImpl( new RegularVariant, players._1, players._2, 3 )
    game.play
  }
}
