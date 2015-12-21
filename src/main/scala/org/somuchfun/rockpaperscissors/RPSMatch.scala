package org.somuchfun.rockpaperscissors

import org.somuchfun.rockpaperscissors.impl.RPSMatchImpl
import org.somuchfun.rockpaperscissors.players.Player

import scala.util.Try


sealed trait PlayerId
sealed trait RoundResult
case object PlayerIdA extends PlayerId with RoundResult {
  override def toString: String = "A"
}
case object PlayerIdB extends PlayerId with RoundResult {
  override def toString: String = "B"
}
case object Draw extends RoundResult

/** Score of a match. Can be also an intermediate score. */
case class Score(scoreA: Int, scoreB: Int, winner: RoundResult)

/** A round that has been played by both players. */
case class CompletedRound(a: Int, b: Int, result: RoundResult)

/** Current status of a game. Includes number of rounds to be played and completed rounds. */
case class Report(completedRounds: Seq[CompletedRound], isFinished: Boolean, currentStep: Int, score: Score)


sealed trait PlayerType
case object TypeHuman extends PlayerType
case object TypeComputer extends PlayerType
case class PlayerDescription(name: String, playerId: PlayerId, playerType: PlayerType)

/**
  * Match API.
  */
trait RPSMatch {
  /**
    * Perform a move by player `playerDef` in this match.
    * @param playerId the player
    * @param step the game round we are in
    * @param choice player's choice
    * @return updates [[Report]]
    */
  def submitMove(playerId: PlayerId, step: Int, choice: Int) : Try[Report]
  
  /** Get information on certain player. */
  def playerInfo(playerId: PlayerId) : PlayerDescription
  
  def computerPlayerMove(playerId: PlayerId) : Try[Int]

  /** A variant: Can be Rock-Scissors-Stone or any modulo-based game. */
  def variant: Variant

  /** Retrieve status of current game. */
  def status: Report
}

/** Factory-like companion object. */
object RPSMatch {
  def apply(variant: Variant, numberOfRounds : Int, playerA: Player, playerB: Player ) : RPSMatch = {
    new RPSMatchImpl(new RegularVariant, numberOfRounds, playerA, playerB)
  }
}