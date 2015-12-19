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

    val rpsMatch = RPSMatch(new RegularVariant, 3)
    
    val presenter = new GamePresenter( players._1, players._2)
    presenter.play(rpsMatch)
  }
}

class GamePresenter(playerA: Player, playerB: Player) {
  def play(rpsMatch: RPSMatch) : Report = {
    val ui = new ConsoleUI(rpsMatch)
    ui.view(rpsMatch.status, playerA.name, playerB.name)
    while (!rpsMatch.status.isFinished) {
      playerA.triggerMove(rpsMatch)
      playerB.triggerMove(rpsMatch)
      ui.view(rpsMatch.status, playerA.name, playerB.name)
    }
    rpsMatch.status
  }
}
