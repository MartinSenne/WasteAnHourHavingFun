package org.somuchfun.rockpaperscissors

import org.somuchfun.rockpaperscissors.players.Player

trait Game {
  /**
    * Create a new match.
    */
  def newMatch(variant: Variant, numberOfRounds : Int, playerA: Player, playerB: Player): RPSMatch
}

/** Factory-like companion object. */
object Game {
  def apply() : Game = {
    new GameImpl
  }
}
