package org.somuchfun.rockpaperscissors

import org.somuchfun.rockpaperscissors.players._
import org.somuchfun.rockpaperscissors.ui.MatchPresenter
import org.somuchfun.rockpaperscissors.ui.views.impl.console.ConsoleViews.{ConsoleSelectPlayerNameView, ConsoleSelectGameTypeView, ConsoleReportView}


object Main {
  def main(args: Array[String]): Unit = {
    val gameType = (new ConsoleSelectGameTypeView).selectGameType()

    val players = gameType match {
      case "a" ⇒ (new ComputerPlayer("Darwin", PlayerIdA), new ComputerPlayer("Watson", PlayerIdB))
      case "b" ⇒ {
        val nameA = (new ConsoleSelectPlayerNameView).selectPlayerName(PlayerIdA)
        (new HumanPlayer(nameA, PlayerIdA), new ComputerPlayer("Watson", PlayerIdB))
      }
      case "c" ⇒ {
        val nameA = (new ConsoleSelectPlayerNameView).selectPlayerName(PlayerIdA)
        val nameB = (new ConsoleSelectPlayerNameView).selectPlayerName(PlayerIdB)
        (new HumanPlayer(nameA, PlayerIdA), new HumanPlayer(nameB, PlayerIdB))
      }
    }

    val rpsMatch = RPSMatch(new RegularVariant, 2, players._1, players._2) // our model
    val reportView = new ConsoleReportView
    
    val presenter = new MatchPresenter(rpsMatch, reportView)
    presenter.play
  }
}


