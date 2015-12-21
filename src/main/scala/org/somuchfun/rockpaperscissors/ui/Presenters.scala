package org.somuchfun.rockpaperscissors.ui

import org.somuchfun.rockpaperscissors.ui.views.AbstractViews._
import org.somuchfun.rockpaperscissors._

import scala.util.{Success, Failure}

object Presenters {

  class MatchPresenter(rpsMatchModel: RPSMatch)(implicit factory: ViewFactory) {
    def go: Report = {
      val elements = rpsMatchModel.variant.elements
      val playerADesc = rpsMatchModel.playerInfo(PlayerIdA)
      val playerBDesc = rpsMatchModel.playerInfo(PlayerIdB)

      // Player move view and presenter
      val playerMoveView = factory.createPlayerMoveView 
      val playerMovePresenterA = new PlayerMovePresenter(playerADesc, rpsMatchModel, playerMoveView)
      val playerMovePresenterB = new PlayerMovePresenter(playerBDesc, rpsMatchModel, playerMoveView)
      
      // Report view and presenter
      val reportView = factory.createReportView 
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
  
  class SelectPlayerNamePresenter(playerId: PlayerId, selectPlayerNameView: SelectPlayerNameView) {
    def go: String = {
      selectPlayerNameView.selectPlayerName(playerId)
    }
  }

  class SelectGameTypePresenter( view: SelectGameTypeView) {
    def go: String = {
      view.selectGameType()
    }
  }
}