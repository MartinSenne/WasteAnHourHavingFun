package org.somuchfun.rockpaperscissors

import org.somuchfun.rockpaperscissors.exceptions._
import org.somuchfun.rockpaperscissors.players._
import org.somuchfun.rockpaperscissors.ui.ConsoleUI

import scala.util.{Failure, Success, Try}


sealed trait PlayerPos
sealed trait RoundResult
case object PlayerPosA extends PlayerPos with RoundResult
case object PlayerPosB extends PlayerPos with RoundResult
case object Draw extends RoundResult

/** Current status of a game. Includes number of round to be played and completed rounds. */
case class GameStatus(playerA: Player, playerB: Player, currentStep : Option[Int], completedRounds: Seq[CompletedRound] )

/** A round that has been played by both players. */
case class CompletedRound(a: Int, b: Int, result: RoundResult)

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
  def submitMove(playerDef: PlayerPos, step: Int, choice: Int) : Try[GameStatus]
  
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


  
  override def submitMove(playerDef: PlayerPos, step: Int, choice: Int) : Try[GameStatus] = {
    // Game is running? None indicates game is over
    state.currentStep match {
      case Some(currentStep) =>

        if (step == currentStep) {
          if (state.currentRound.movePossible(playerDef)) {
            updateState(playerDef, step, choice)
            Success(gameStatus)
          } else {
            Failure(new MoveAlreadyMadeException("Move already made."))
          }
        } else {
          Failure(new IncorrectRoundNumberException("Submitting move that does not have the correct step number."))
        }
        
      case None => Failure(new GameAlreadyFinishedException("Game is already over.")) 
    }
  }

  def play: GameStatus = {
    val ui = new ConsoleUI(this)
    ui.view(gameStatus)
    while (state.currentStep.isDefined) {
      playerA.triggerMove(this)
      playerB.triggerMove(this)
      ui.view(gameStatus)
    }
    gameStatus
  }
  
  // Internal =====================================================

  /**
    * Case class representing the current state.
    * @param currentStep is the round number that is actually played
    * @param currentRound collects moves of player A and B.
    * @param completedRounds is a list of rounds that have been completed.
    */
  case class State(currentStep : Option[Int], currentRound: Round, completedRounds: Seq[CompletedRound])

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
    
    def movePossible( playerDef: PlayerPos ) : Boolean = playerDef match {
      case PlayerPosA ⇒ choiceA.isEmpty
      case PlayerPosB ⇒ choiceB.isEmpty
    }
    def move(playerDef: PlayerPos, choice: Int ) : Round = playerDef match {
      case PlayerPosA ⇒ this.copy(choiceA = Some(choice))
      case PlayerPosB ⇒ this.copy(choiceB = Some(choice))
    }
    
    def toCompletedRound : Option[CompletedRound] = {
      for {
        a <- choiceA
        b <- choiceB
      } yield CompletedRound(a, b, variant.beats(a,b))
    }
  }

  private def updateState(playerDef: PlayerPos, step: Int, choice: Int) : Unit = {
    val updatedRound = state.currentRound.move(playerDef, choice)
    
    state = updatedRound.toCompletedRound match {
      case Some( completedRound ) => 
        val newRound = Round(None, None)
        // warning: this add operation is costly ( O(n) ) !!!
        val newCompletedMoves = state.completedRounds :+ completedRound
        val newStep = for {
          i <- state.currentStep
          if (moreRoundsToPlay(newCompletedMoves))
        } yield i+1
        
      State( newStep, newRound, newCompletedMoves)

      case None => {
        state.copy(currentRound = updatedRound)
      }
    } 
  }

  private def moreRoundsToPlay(completedMoves: Seq[CompletedRound]) : Boolean = completedMoves.filter(round => round.result != Draw ).size < rounds  
 
}

object Main {
  def main(args: Array[String]) : Unit = {
    val gameType = ConsoleUI.selectGameType()
    
    val players = gameType match {
      case "a" ⇒ (new ComputerPlayer("Darwin", PlayerPosA), new ComputerPlayer("Watson", PlayerPosB))
      case "b" ⇒ {
        val nameA = ConsoleUI.queryPlayerName( PlayerPosA )
        (new HumanPlayer(nameA, PlayerPosA), new ComputerPlayer("Watson", PlayerPosB))
      }
      case "c" ⇒ {
        val nameA = ConsoleUI.queryPlayerName( PlayerPosA )
        val nameB = ConsoleUI.queryPlayerName( PlayerPosB )
        (new HumanPlayer(nameA, PlayerPosA), new HumanPlayer(nameB, PlayerPosB))
      }
    }
    
    val game = new GameImpl( new RegularVariant, players._1, players._2, 3 )
    game.play
  }
}
