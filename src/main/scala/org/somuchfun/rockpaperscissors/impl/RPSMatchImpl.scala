package org.somuchfun.rockpaperscissors.impl

import org.somuchfun.rockpaperscissors._
import org.somuchfun.rockpaperscissors.exceptions._
import org.somuchfun.rockpaperscissors.players._

import scala.util.{Failure, Success, Try}

/**
  * State based (non-actor based) implementation of a [[RPSMatch]].
  * @param variant specifies the variant to use.
  * @param rounds number of rounds to play.
  */
class RPSMatchImpl(val variant: Variant, val rounds: Int) extends RPSMatch {

  // API side =================================================
  override def status : Report = state match {
    case Running(step, _, completedRounds) => Report(completedRounds, false, step, null)
    case Finished(score, completedRounds) => Report(completedRounds, true, 0, score)
  }

  override def submitMove(playerPos: PlayerId, submittedStep: Int, choice: Int) : Try[Report] = {
    val tryUpdatedRound = for {
      (step, round) <- Validators.isGameRunning
      _ <- Validators.roundMatches(submittedStep, step)
      updatedRound <- Validators.canExecuteMove( playerPos, choice, round)
    } yield updatedRound

    tryUpdatedRound match {
      case Success(updatedRound) => {
        state = advance(state, updatedRound)
        Success( status )
      }
      case Failure(ex) => Failure(ex)
    }
  }

  // Internal =====================================================
  object Validators {
    def isGameRunning: Try[(Int, Round)] = state match {
      case Running(step, round, _) => Success( (step, round) )
      case Finished(_, _) => Failure(new GameAlreadyFinishedException("Game is already over."))
    }    
    
    def roundMatches(submittedStep: Int, currentStep: Int) : Try[Unit] =
      if (submittedStep == currentStep) Success() else Failure(new IncorrectRoundNumberException("Move does not have expected round number."))

    def canExecuteMove(playerId: PlayerId, choice: Int, round: Round) : Try[Round] = {
      val f = Failure(new MoveAlreadyMadeException("Move is already made."))
      playerId match {
        case PlayerIdA => if (round.choiceA.isEmpty) Success(round.copy(choiceA = Some(choice))) else f
        case PlayerIdB => if (round.choiceB.isEmpty) Success(round.copy(choiceB = Some(choice))) else f
      }
    }
  }

  sealed trait GameState
  case class Running(currentStep: Int, currentRound: Round, completedRounds: Seq[CompletedRound]) extends GameState
  case class Finished(score: Score, completedRounds: Seq[CompletedRound]) extends GameState

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
    * Initial state of a match 
    * - start in round 1, 
    * - no choices have been made and 
    * - no completed rounds exists for this match so far.
    *
    * REMARK: As we are NOT allowed to use any Framework, we use a central (but mutable) place to keep our state.
    * Basically, we should use an Actor of the Akka framework to encapsulate that state.
    */
  private var state: GameState = Running(1, Round(None, None), List())

  private def advance(state: GameState, updatedRound: Round) : GameState = {
    state match {
      case running : Running => {
        updatedRound.optCompletedRound match {
          case Some(completedRound) => { // round completed
            // warning: this add operation is costly ( O(n) ) !!!
            val completedRounds = running.completedRounds :+ completedRound
            if (moreRoundsToPlay(completedRounds)) {
              Running(running.currentStep + 1, Round(None, None), completedRounds)
            } else {
              Finished(calculateScore(completedRounds), completedRounds)
            }
          }

          case None => { // round not completed (e.g. other player's move is missing)
            running.copy(currentRound = updatedRound)
          }
        }
      }
      case Finished(_, _) => throw new RuntimeException("This should be detected earlier.")
    }
  }
  
  private def calculateScore( completedRounds: Seq[CompletedRound] ) : Score = {
    val scoreA = completedRounds.filter(cm ⇒ cm.result.equals(PlayerIdA) ).size
    val scoreB = completedRounds.filter(cm ⇒ cm.result.equals(PlayerIdB) ).size
    val winner = if (scoreA > scoreB) PlayerIdA else {if (scoreB > scoreA) PlayerIdB else Draw}
    Score(scoreA, scoreB, winner)
  }

  private def moreRoundsToPlay(completedRounds: Seq[CompletedRound]) : Boolean = {
    val s = calculateScore(completedRounds)
    (s.scoreA < rounds) && (s.scoreB < rounds)
  }
}
