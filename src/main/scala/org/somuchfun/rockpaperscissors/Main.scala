package org.somuchfun.rockpaperscissors

import org.somuchfun.rockpaperscissors.players._
import org.somuchfun.rockpaperscissors.ui.Presenters._
import org.somuchfun.rockpaperscissors.ui.views.AbstractViews._
import org.somuchfun.rockpaperscissors.ui.views.impl.console.ConsoleViews._


object Main {
  def main(args: Array[String]): Unit = {
    val control = new GameControl(ConsoleViewFactory)
    control.start
  }
}

trait ViewFactory {
  def createReportView : ReportView
  def createSelectGameTypeView : SelectGameTypeView
  def createSelectPlayerNameView : SelectPlayerNameView
  def createPlayerMoveView : PlayerMoveView
}

object ConsoleViewFactory extends ViewFactory {
  override def createReportView: ReportView = new ConsoleReportView
  override def createPlayerMoveView: PlayerMoveView = new ConsolePlayerMoveView
  override def createSelectGameTypeView: SelectGameTypeView = new ConsoleSelectGameTypeView
  override def createSelectPlayerNameView: SelectPlayerNameView = new ConsoleSelectPlayerNameView
}

class GameControl(viewFactory: ViewFactory) {
  def start: Report = {
    val selectGameTypePresenter = new SelectGameTypePresenter(viewFactory.createSelectGameTypeView)
    val gameType = selectGameTypePresenter.go

    val players = gameType match {
      case "a" ⇒ (new ComputerPlayer("Darwin", PlayerIdA), new ComputerPlayer("Watson", PlayerIdB))
      case "b" ⇒ {
        val nameA = new SelectPlayerNamePresenter(PlayerIdA, viewFactory.createSelectPlayerNameView).go
        (new HumanPlayer(nameA, PlayerIdA), new ComputerPlayer("Watson", PlayerIdB))
      }
      case "c" ⇒ {
        val nameA = new SelectPlayerNamePresenter(PlayerIdA, viewFactory.createSelectPlayerNameView).go
        val nameB = new SelectPlayerNamePresenter(PlayerIdB, viewFactory.createSelectPlayerNameView).go
        (new HumanPlayer(nameA, PlayerIdA), new HumanPlayer(nameB, PlayerIdB))
      }
    }

    val rpsMatch = RPSMatch(new RegularVariant, 2, players._1, players._2) // our model

    val presenter = new MatchPresenter(rpsMatch)
    presenter.go
  }
}


