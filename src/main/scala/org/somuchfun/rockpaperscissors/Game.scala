package org.somuchfun.rockpaperscissors

import org.somuchfun.rockpaperscissors.impl.GameImpl
import org.somuchfun.rockpaperscissors.players.Player

import scala.util.Try


sealed trait PlayerId
sealed trait RoundResult
case object PlayerIdA extends PlayerId with RoundResult
case object PlayerIdB extends PlayerId with RoundResult
case object Draw extends RoundResult

case class Score(scoreA: Int, scoreB: Int, winner: PlayerId)

/** A round that has been played by both players. */
case class CompletedRound(a: Int, b: Int, result: RoundResult)

/** Current status of a game. Includes number of rounds to be played and completed rounds. */
case class Report(playerA: Player, playerB: Player, completedRounds: Seq[CompletedRound], isFinished: Boolean, currentStep: Int, score: Score)


/**
  * Game API.
  */
trait Game {
  /**
    * Perform a game move by player `playerDef`.
    * @param playerDef the player
    * @param step the game round we are in
    * @param choice player's choice
    * @return updates [[Report]]
    */
  def submitMove(playerDef: PlayerId, step: Int, choice: Int) : Try[Report]

  /** A variant: Can be Rock-Scissors-Stone or any modulo-based game. */
  def variant: GameVariant

  /** Retrieve status of current game. */
  def status: Report
    
  /** First player. */
  def playerA: Player

  /** Second player. */
  def playerB: Player
}

/** Factory-like companion object. */
object Game {
  def apply( variant: GameVariant, playerA: Player, playerB: Player, numberOfRounds : Int ) : Game = {
    new GameImpl(new RegularVariant, playerA, playerB, 3)
  }
}