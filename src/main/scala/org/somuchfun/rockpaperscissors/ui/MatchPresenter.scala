package org.somuchfun.rockpaperscissors.ui

import org.somuchfun.rockpaperscissors.ui.views.AbstractViews._
import org.somuchfun.rockpaperscissors._
import org.somuchfun.rockpaperscissors.players.Player
import org.somuchfun.rockpaperscissors.ui.views.impl.console.ConsoleViews.ConsolePlayerMoveView

import scala.util.{Success, Failure, Try}

class MatchPresenter(rpsMatchModel: RPSMatch, reportView: ReportView) {
  def play : Report = {
    val elements = rpsMatchModel.variant.elements
    val playerADesc = rpsMatchModel.playerInfo(PlayerIdA)     
    val playerBDesc = rpsMatchModel.playerInfo(PlayerIdB)    
    
    val mpA = new PlayerMovePresenter( rpsMatchModel.playerInfo(PlayerIdA ), rpsMatchModel, new ConsolePlayerMoveView(elements))
    val mpB = new PlayerMovePresenter( rpsMatchModel.playerInfo(PlayerIdB ), rpsMatchModel, new ConsolePlayerMoveView(elements))
    
    reportView.show(rpsMatchModel.status, playerADesc.name, playerBDesc.name, elements)
    while (!rpsMatchModel.status.isFinished) {
      mpA.go
      mpB.go
//      playerA.triggerMove(rpsMatchModel)
//      playerB.triggerMove(rpsMatchModel)
      reportView.show(rpsMatchModel.status, playerADesc.name, playerBDesc.name, elements)
    }
    rpsMatchModel.status
  }
}

class PlayerMovePresenter(pd: PlayerDescription, rpsMatchModel: RPSMatch, view: PlayerMoveView ) {
  def go : Unit = {
    val choice = if (pd.playerType == TypeComputer) {
      val computerChoice = rpsMatchModel.computerPlayerMove(pd.playerId) match {
        case Success(i) => i
        case Failure(ex) => {
          throw new RuntimeException("Problem invoking computer move. Aborting.")
        }
      }
      view.showMove(pd, computerChoice)
      computerChoice
    } else {
      view.selectMove(pd)
    }

    rpsMatchModel.submitMove(pd.playerId, rpsMatchModel.status.currentStep, choice)
  }
}