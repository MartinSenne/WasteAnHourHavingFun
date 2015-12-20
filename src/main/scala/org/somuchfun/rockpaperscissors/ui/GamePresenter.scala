package org.somuchfun.rockpaperscissors.ui

import org.somuchfun.rockpaperscissors.ui.views.AbstractViews._
import org.somuchfun.rockpaperscissors.{Report, RPSMatch}
import org.somuchfun.rockpaperscissors.players.Player

class GamePresenter(rpsMatchModel: RPSMatch, reportView: ReportView, playerA: Player, playerB: Player) {
  def play : Report = {
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