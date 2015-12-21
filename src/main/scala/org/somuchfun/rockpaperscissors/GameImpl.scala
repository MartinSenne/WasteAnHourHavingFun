package org.somuchfun.rockpaperscissors

import org.somuchfun.rockpaperscissors.players.Player

class GameImpl extends Game {
  override def newMatch(variant: Variant, numberOfWonRounds: Int, playerA: Player, playerB: Player): RPSMatch = 
    RPSMatch(variant, numberOfWonRounds, playerA, playerB)
}
