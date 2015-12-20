package org.somuchfun.rockpaperscissors.ui

import org.somuchfun.rockpaperscissors.ui.impl.ConsoleReportView
import org.somuchfun.rockpaperscissors.{Report, RPSMatch}
import org.somuchfun.rockpaperscissors.players.Player

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