package org.somuchfun.rockpaperscissors.fixture

import org.somuchfun.rockpaperscissors.players.Player
import org.somuchfun.rockpaperscissors.{RegularVariant, PlayerIdB, PlayerIdA, RPSMatch}
import org.somuchfun.rockpaperscissors.MockPlayers.DummyPlayer

object PreparedMatch {
  def apply() : RPSMatch = {
    val playerA = new DummyPlayer(PlayerIdA)
    val playerB = new DummyPlayer(PlayerIdB)
    this.apply(playerA, playerB)
  }
  
  def apply( playerA: Player, playerB: Player ) : RPSMatch = {
    val game = RPSMatch(new RegularVariant, 3)

    game.submitMove(PlayerIdA, 1, 1)
    game.submitMove(PlayerIdB, 1, 3)

    game
  }
}
