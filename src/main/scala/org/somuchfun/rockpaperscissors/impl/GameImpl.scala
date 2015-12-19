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
  override def status : Report = state match {
    case Running(step, _, completedRounds) => Report(playerA, playerB, completedRounds, false, step, null)
    // TODO: GameReport is bad in its representation
    case Finished(score, completedRounds) => Report(playerA, playerB, completedRounds, true, 0, score)
  }

  override def submitMove(playerPos: PlayerId, submittedStep: Int, choice: Int) : Try[Report] = {
    val tryUpdatedRound = for {
      (step, round) <- Validators.isGameRunning
      _ <- Validators.roundMatches(submittedStep, step)
      updatedRound <- Validators.executeMove( playerPos, choice, round)
    } yield updatedRound

    tryUpdatedRound match {
      case Success(updatedRound) => {
        state = advance(state, updatedRound)
        Success( status )
      }
      case Failure(ex) => Failure(ex)
    }
  }

//  /** Retrieve information about the score and the winner of a decided match. */
//  override def finalResult: Try[Score] = {
//    state match {
//      case Finished(score, _) => Success(score)  
//      case r:Running => Failure(new IllegalStateException("Match not finished."))
//    }
//  }

  // Internal =====================================================
  object Validators {
    def isGameRunning: Try[(Int, Round)] = state match {
      case Running(step, round, _) => Success( (step, round) )
      case Finished(_, _) => Failure(new GameAlreadyFinishedException("Game is already over."))
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
    * Initial state is set to round 1, where no choices have been made, and no completed rounds are present so far.
    *
    * REMARK: As we are not allowed to use any Framework, we use a central place to keep our state.
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
    val winner = if (scoreA > scoreB) PlayerIdA else {if (scoreB > scoreA) PlayerIdB else throw new RuntimeException("Draw, still it can not be.")}
    Score(scoreA, scoreB, winner)
  }

  private def moreRoundsToPlay(completedMoves: Seq[CompletedRound]) : Boolean = completedMoves.filter(round => round.result != Draw ).size < rounds
}
