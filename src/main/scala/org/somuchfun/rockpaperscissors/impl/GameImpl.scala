package org.somuchfun.rockpaperscissors.impl

import org.somuchfun.rockpaperscissors._
import org.somuchfun.rockpaperscissors.exceptions._
import org.somuchfun.rockpaperscissors.players._

import scala.util.{Failure, Success, Try}

/**
  * State based (non-actor based) implementation of a [[Game]].
  * @param variant specifies the variant to use.
  * @param playerA first player (A) according to [[Player]]
  * @param playerB second player (B) according to [[Player]]
  * @param rounds number of rounds to play.
  */
class GameImpl(val variant: GameVariant, val playerA: Player, val playerB: Player, val rounds: Int) extends Game {

  // API side =================================================
  override def report : GameReport = gameState match {
    case Running( step, _, completedRounds) => GameReport(playerA, playerB, Some(step), completedRounds)
    case Finished( scoreA, scoreB, completedRounds) => GameReport(playerA, playerB, None, completedRounds)
  }

  override def submitMove(playerPos: PlayerId, submittedStep: Int, choice: Int) : Try[GameReport] = {
    val tryUpdatedRound = for {
      (step, round) <- Validators.isGameRunning
      _ <- Validators.roundMatches(submittedStep, step)
      updatedRound <- Validators.executeMove( playerPos, choice, round)
    } yield updatedRound

    tryUpdatedRound match {
      case Success(updatedRound) => {
        gameState = advance(gameState, updatedRound)
        Success( report )
      }
      case Failure(ex) => Failure(ex)
    }
  }

  // Internal =====================================================
  object Validators {
    def isGameRunning: Try[(Int, Round)] = gameState match {
      case Running(step, round, _) => Success( (step, round) )
      case Finished(_, _, _) => Failure(new GameAlreadyFinishedException("Game is already over."))
    }    
    
    def roundMatches(submittedStep: Int, currentStep: Int) : Try[Unit] =
      if (submittedStep == currentStep) Success() else Failure(new IncorrectRoundNumberException("Move does not have expected round number."))

    def executeMove(playerId: PlayerId, choice: Int, round: Round) : Try[Round] = {
      val f = Failure(new MoveAlreadyMadeException("Move is already made."))
      playerId match {
        case PlayerIdA => if (round.choiceA.isEmpty) Success(round.copy(choiceA = Some(choice))) else f
        case PlayerIdB => if (round.choiceB.isEmpty) Success(round.copy(choiceB = Some(choice))) else f
      }
    }
  }

  sealed trait GameState
  case class Running(currentStep: Int, currentRound: Round, completedRounds: Seq[CompletedRound]) extends GameState
  case class Finished(scoreA: Int, scoreB: Int, completedRounds: Seq[CompletedRound]) extends GameState

  case class Round(choiceA: Option[Int], choiceB: Option[Int] ) {
    /** Create a [[CompletedRound]] if possible. */
    def optCompletedRound : Option[CompletedRound] = {
      for {
        a <- choiceA
        b <- choiceB
      } yield CompletedRound(a, b, variant.beats(a,b))
    }
  }

  /**
    * State of the game.
    * Initial gameState is set to round 1, where no choices have been made, and no completed rounds are present so far.
    *
    * REMARK: As we are not allowed to use any Framework, we use a central place to keep our gameState.
    * Basically, we should use an Actor of the Akka framework to encapsulate that gameState.
    */
  private var gameState: GameState = Running(1, Round(None, None), List())

  private def advance(state: GameState, updatedRound: Round) : GameState = {
    state match {
      case running@Running(_, _, completedRounds) => {
        updatedRound.optCompletedRound match {
          case Some(completedRound) => {
            // warning: this add operation is costly ( O(n) ) !!!
            val newCompletedMoves = completedRounds :+ completedRound
            if (moreRoundsToPlay(newCompletedMoves)) {
              Running(running.currentStep + 1, Round(None, None), newCompletedMoves)
            } else {
              val (scoreA, scoreB) = (3, 5)
              Finished(scoreA, scoreB, newCompletedMoves)
            }
          }

          case None => {
            running.copy(currentRound = updatedRound)
          }
        }
      }
      case Finished(_, _, _) => throw new RuntimeException("This should be detected earlier.")
    }
  }

  private def moreRoundsToPlay(completedMoves: Seq[CompletedRound]) : Boolean = completedMoves.filter(round => round.result != Draw ).size < rounds
}
