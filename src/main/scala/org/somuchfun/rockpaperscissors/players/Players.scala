package org.somuchfun.rockpaperscissors.players

import org.somuchfun.rockpaperscissors.ui.views.impl.console.ConsoleViews.ConsoleSelectMoveView
import org.somuchfun.rockpaperscissors.{PlayerId, RPSMatch}

trait Player {
  
  def name: String
  
  def playerId: PlayerId

  /**
    * This method is used to trigger a move.
    * Remark: In a real client-server app this would not be necessary as the client runs autarkicly.
    * @param game describes the current game
    */
  def triggerMove(game: RPSMatch): Unit = {
    val gameRepr = game.status
    if (!gameRepr.isFinished) {
      val choice = nextChoice(game)
      game.submitMove(playerId, gameRepr.currentStep, choice)
    }
    else {
      throw new RuntimeException("Game is already finished. Trigger should never be called in that case.")
    }
  }
  
  /** Determine next move. */
  def nextChoice(game: RPSMatch): Int
}

/**
  * A human player.
  * @param name is player's name
  * @param playerId is the role (either player A or player B)
  */
class HumanPlayer(val name: String, val playerId: PlayerId) extends Player {
  override def nextChoice(game: RPSMatch): Int = {
    (new ConsoleSelectMoveView).selectMove(playerId, game.variant.elements)
      // selectAMove(game)
  }
}

/**
  * A computer player.
  * @param nameAdd is an suffix to the name "Computer" of a computer player.
  * @param playerId is the role (either player A or player B)
  */
class ComputerPlayer(nameAdd: String, val playerId: PlayerId) extends Player {
  import scala.util.Random
  
  val rnd = new Random()
  val name = "Computer " + nameAdd

  override def nextChoice(game: RPSMatch): Int = {
    Thread.sleep(500)
    val choice = rnd.nextInt(game.variant.n) + 1
    choice
  }
}