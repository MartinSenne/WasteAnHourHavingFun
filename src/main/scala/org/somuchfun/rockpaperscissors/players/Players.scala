package org.somuchfun.rockpaperscissors.players

import org.somuchfun.rockpaperscissors.ui.ConsoleUI
import org.somuchfun.rockpaperscissors.{PlayerDef, Game}

trait Player {
  
  def name: String
  
  def playerDef: PlayerDef

  /**
    * This method is used to trigger a move.
    * Remark: In a real client-server app this would not be necessary as the client runs autarcly.
    * @param game describes the current game
    */
  def triggerMove(game: Game): Unit = {
    val gameStat = game.gameStatus
    gameStat.currentStep match {
      case Some(step) ⇒ {
        val choice = nextChoice( game )
        // println(s"submit : ${playerDef}, ${step}, ${choice}")
        game.submitMove(playerDef, step, choice)
      }
      case None ⇒ {
        throw new RuntimeException("Game is already finished. Trigger should never be called in that case.")
      }  
    }
  }
  
  /** Determine next move. */
  def nextChoice(game: Game): Int
}

/**
  * A human player.
  * @param name is player's name
  * @param playerDef is the role (either player A or player B)
  */
class HumanPlayer(val name: String, val playerDef: PlayerDef) extends Player {
  override def nextChoice(game: Game): Int = {
    
    ConsoleUI.userChoiceForMove(game)
  }
}

/**
  * A computer player.
  * @param nameAdd is an suffix to the name "Computer" of a computer player.
  * @param playerDef is the role (either player A or player B)
  */
class ComputerPlayer(nameAdd: String, val playerDef: PlayerDef) extends Player {
  import scala.util.Random
  
  val rnd = new Random()
  val name = "Computer " + nameAdd

  override def nextChoice(game: Game): Int = {
    Thread.sleep(500)
    val choice = rnd.nextInt(game.variant.n) + 1
    choice
  }
}