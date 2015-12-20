package org.somuchfun.rockpaperscissors

import org.somuchfun.rockpaperscissors.players._
import org.somuchfun.rockpaperscissors.ui.GamePresenter
import org.somuchfun.rockpaperscissors.ui.impl.GenericConsoleViews

object Main {
  def main(args: Array[String]): Unit = {
    val gameType = GenericConsoleViews.selectGameType()

    val players = gameType match {
      case "a" ⇒ (new ComputerPlayer("Darwin", PlayerIdA), new ComputerPlayer("Watson", PlayerIdB))
      case "b" ⇒ {
        val nameA = GenericConsoleViews.selectPlayerName(PlayerIdA)
        (new HumanPlayer(nameA, PlayerIdA), new ComputerPlayer("Watson", PlayerIdB))
      }
      case "c" ⇒ {
        val nameA = GenericConsoleViews.selectPlayerName(PlayerIdA)
        val nameB = GenericConsoleViews.selectPlayerName(PlayerIdB)
        (new HumanPlayer(nameA, PlayerIdA), new HumanPlayer(nameB, PlayerIdB))
      }
    }

    val rpsMatch = RPSMatch(new RegularVariant, 3)
    
    val presenter = new GamePresenter( rpsMatch, players._1, players._2)
    presenter.play
  }
}




