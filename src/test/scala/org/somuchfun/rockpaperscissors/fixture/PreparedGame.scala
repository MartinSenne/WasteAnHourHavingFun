package org.somuchfun.rockpaperscissors.fixture

import org.somuchfun.rockpaperscissors.players.Player
import org.somuchfun.rockpaperscissors.{RegularVariant, PlayerIdB, PlayerIdA, Game}
import org.somuchfun.rockpaperscissors.MockPlayers.DummyPlayer

object PreparedGame {
  def apply() : Game = {
    val playerA = new DummyPlayer(PlayerIdA)
    val playerB = new DummyPlayer(PlayerIdB)
    this.apply(playerA, playerB)
  }
  
  def apply( playerA: Player, playerB: Player ) : Game = {
    val game = Game(new RegularVariant, playerA, playerB, 3)

    game.submitMove(PlayerIdA, 1, 1)
    game.submitMove(PlayerIdB, 1, 3)

    game
  }
}
