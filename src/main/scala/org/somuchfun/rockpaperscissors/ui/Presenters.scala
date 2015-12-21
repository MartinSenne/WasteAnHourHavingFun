package org.somuchfun.rockpaperscissors.ui

import org.somuchfun.rockpaperscissors.ui.views.AbstractViews._
import org.somuchfun.rockpaperscissors._
import org.somuchfun.rockpaperscissors.ui.views.impl.console.ConsoleViews.{ConsoleReportView, ConsolePlayerMoveView}

import scala.util.{Success, Failure, Try}

object Presenters {

  class MatchPresenter(rpsMatchModel: RPSMatch) {
    def go: Report = {
      val elements = rpsMatchModel.variant.elements
      val playerADesc = rpsMatchModel.playerInfo(PlayerIdA)
      val playerBDesc = rpsMatchModel.playerInfo(PlayerIdB)

      // Player move view and presenter
      val playerMoveView = new ConsolePlayerMoveView
      val playerMovePresenterA = new PlayerMovePresenter(playerADesc, rpsMatchModel, playerMoveView)
      val playerMovePresenterB = new PlayerMovePresenter(playerBDesc, rpsMatchModel, playerMoveView)
      
      // Report view and presenter
      val reportView = new ConsoleReportView
      val reportPresenter = new ReportPresenter(rpsMatchModel, reportView)

      do {
        playerMovePresenterA.go
        playerMovePresenterB.go
        reportPresenter.go
      } while (!rpsMatchModel.status.isFinished)
      
      rpsMatchModel.status
    }
  }

  class PlayerMovePresenter(pd: PlayerDescription, rpsMatchModel: RPSMatch, view: PlayerMoveView) {
    def go: Unit = {
      val elements = rpsMatchModel.variant.elements
      val choice = if (pd.playerType == TypeComputer) {
        val computerChoice = rpsMatchModel.computerPlayerMove(pd.playerId) match {
          case Success(i) => i
          case Failure(ex) => {
            throw new RuntimeException("Problem invoking computer move. Aborting.")
          }
        }
        view.showMove(pd, computerChoice, elements)
        computerChoice
      } else {
        view.selectMove(pd, elements)
      }

      rpsMatchModel.submitMove(pd.playerId, rpsMatchModel.status.currentStep, choice)
    }
  }
  
  class ReportPresenter(rpsMatchModel: RPSMatch, reportView: ReportView) {
    def go : Unit = {
      val playerADesc = rpsMatchModel.playerInfo(PlayerIdA)
      val playerBDesc = rpsMatchModel.playerInfo(PlayerIdB)
      val report = rpsMatchModel.status
      val elements = rpsMatchModel.variant.elements
      reportView.show( report, playerADesc.name, playerBDesc.name, elements)
    }
  }
}