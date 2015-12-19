package org.somuchfun.rockpaperscissors

import org.somuchfun.rockpaperscissors.players._
import org.somuchfun.rockpaperscissors.ui.{ConsoleUI, ConsoleInput}

object Main {
  def main(args: Array[String]): Unit = {
    val gameType = ConsoleInput.selectGameType()

    val players = gameType match {
      case "a" ⇒ (new ComputerPlayer("Darwin", PlayerIdA), new ComputerPlayer("Watson", PlayerIdB))
      case "b" ⇒ {
        val nameA = ConsoleInput.selectPlayerName(PlayerIdA)
        (new HumanPlayer(nameA, PlayerIdA), new ComputerPlayer("Watson", PlayerIdB))
      }
      case "c" ⇒ {
        val nameA = ConsoleInput.selectPlayerName(PlayerIdA)
        val nameB = ConsoleInput.selectPlayerName(PlayerIdB)
        (new HumanPlayer(nameA, PlayerIdA), new HumanPlayer(nameB, PlayerIdB))
      }
    }

    val game = Game(new RegularVariant, players._1, players._2, 3)
    GameSteering.play(game)
  }
}

object GameSteering {
  def play(game: Game) : Report = {
    val ui = new ConsoleUI(game)
    ui.view(game.status)
    while (!game.status.isFinished) {
      game.playerA.triggerMove(game)
      game.playerB.triggerMove(game)
      ui.view(game.status)
    }
    game.status
  }
}
