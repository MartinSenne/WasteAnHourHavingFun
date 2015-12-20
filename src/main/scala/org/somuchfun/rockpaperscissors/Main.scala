package org.somuchfun.rockpaperscissors

import org.somuchfun.rockpaperscissors.players._
import org.somuchfun.rockpaperscissors.ui.{ConsoleReportView, ConsoleInput}

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
    
    val presenter = new GamePresenter( rpsMatch, players._1, players._2)
    presenter.play
  }
}

class GamePresenter(rpsMatchModel: RPSMatch, playerA: Player, playerB: Player) {
  def play : Report = {
    val reportView = new ConsoleReportView // ConsoleUI(rpsMatchModel)
    val elements = rpsMatchModel.variant.elements
    reportView.show(rpsMatchModel.status, playerA.name, playerB.name, elements)
    while (!rpsMatchModel.status.isFinished) {
      playerA.triggerMove(rpsMatchModel)
      playerB.triggerMove(rpsMatchModel)
      reportView.show(rpsMatchModel.status, playerA.name, playerB.name, elements)
    }
    rpsMatchModel.status
  }
}

trait ReportView {
  def show(report: Report, playerAName: String, playerBName: String, elements: Map[Int, String])
}
