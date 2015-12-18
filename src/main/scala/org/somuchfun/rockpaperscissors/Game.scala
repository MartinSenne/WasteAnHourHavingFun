package org.somuchfun.rockpaperscissors

import org.somuchfun.rockpaperscissors.impl.GameImpl
import org.somuchfun.rockpaperscissors.players.Player

import scala.util.Try


sealed trait PlayerId
sealed trait RoundResult
case object PlayerIdA extends PlayerId with RoundResult
case object PlayerIdB extends PlayerId with RoundResult
case object Draw extends RoundResult

/** Current status of a game. Includes number of round to be played and completed rounds. */
case class GameReport(playerA: Player, playerB: Player, currentStep : Option[Int], completedRounds: Seq[CompletedRound] )

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
    * @return updates [[GameReport]]
    */
  def submitMove(playerDef: PlayerId, step: Int, choice: Int) : Try[GameReport]

  /** A variant: Can be Rock-Scissors-Stone or any modulo-based game. */
  def variant: GameVariant

  /** Retrieve status of current game. */
  def report: GameReport
  
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