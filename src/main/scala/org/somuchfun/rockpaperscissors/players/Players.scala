package org.somuchfun.rockpaperscissors.players

import org.somuchfun.rockpaperscissors._

trait Player {
  
  def name: String
  
  def playerId: PlayerId
  
  def playerType: PlayerType
  
  def asDescription: PlayerDescription = PlayerDescription( name, playerId, playerType )
//
//  /**
//    * This method is used to trigger a move.
//    * Remark: In a real client-server app this would not be necessary as the client runs autarkicly.
//    * @param game describes the current game
//    */
//  def triggerMove(game: RPSMatch): Unit = {
//    val gameRepr = game.status
//    if (!gameRepr.isFinished) {
//      val choice = nextChoice(game)
//      game.submitMove(playerId, gameRepr.currentStep, choice)
//    }
//    else {
//      throw new RuntimeException("Game is already finished. Trigger should never be called in that case.")
//    }
//  }
//  
  /** Determine next move. */
  def nextChoice(game: RPSMatch): Int
}

/**
  * A human player.
  * @param name is player's name
  * @param playerId is the role (either player A or player B)
  */
class HumanPlayer(val name: String, val playerId: PlayerId) extends Player {
  val playerType = TypeHuman

  override def nextChoice(game: RPSMatch): Int = {
    throw new RuntimeException("A human player does not provide automatic moves.")
  }
//  override def nextChoice(game: RPSMatch): Int = {
//    (new ConsolePlayerMoveView).selectMove(playerId, game.variant.elements)
//      // selectAMove(game)
//  }
  /** Determine next move. */
  
}

/**
  * A computer player.
  * @param nameAdd is an suffix to the name "Computer" of a computer player.
  * @param playerId is the role (either player A or player B)
  */
class ComputerPlayer(nameAdd: String, val playerId: PlayerId) extends Player {
  import scala.util.Random
  
  val playerType = TypeComputer
  val rnd = new Random()
  val name = "Computer " + nameAdd

  def nextChoice(game: RPSMatch): Int = {
    Thread.sleep(500)
    val choice = rnd.nextInt(game.variant.n) + 1
    choice
  }
}