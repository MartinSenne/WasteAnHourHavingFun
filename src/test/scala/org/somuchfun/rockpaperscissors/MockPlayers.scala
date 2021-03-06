package org.somuchfun.rockpaperscissors

import org.somuchfun.rockpaperscissors.players.Player

/**
  * Created by martin on 19.12.15.
  */
object MockPlayers {

  class SequencePlayer(val nameAdd: String, val playerId: PlayerId, sequence: Seq[Int]) extends Player {

    val name = "SequencePlayer " + nameAdd

    val it = sequence.iterator

    /** Determine next move. */
    override def nextChoice(game: RPSMatch): Int = {
      val choice = if (it.hasNext) it.next else 0
      println(choice)
      choice
    }

    override def playerType: PlayerType = TypeComputer
  }

  class DummyPlayer(val playerId: PlayerId) extends Player {
    val name = "DummyPlayer " + playerId

    /** Determine next move. */
    override def nextChoice(game: RPSMatch): Int = 0

    override def playerType: PlayerType = TypeComputer
  }
}
