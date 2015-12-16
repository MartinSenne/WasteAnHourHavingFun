package org.somuchfun.rockpaperscissors.players

import org.somuchfun.rockpaperscissors.{PlayerDef, Game}

import scala.io.StdIn
import scala.util.Random

trait Player {
  def name: String
  def triggerMove(game: Game, step: Int)
}

class HumanPlayer(val name: String, playerDef: PlayerDef) extends Player {
  override def triggerMove(game: Game, step: Int): Unit = {
    print("Your move: ")
    val choice = StdIn.readLine().toInt
    game.submitMove(playerDef, step, choice)
  }
}

class ComputerPlayer(playerDef: PlayerDef ) extends Player {
  val rnd = new Random()
  val name = "Computer"

  override def triggerMove(game: Game, step: Int): Unit = {
    Thread.sleep(500)
    val choice = rnd.nextInt( game.variant.n ) + 1
    game.submitMove(playerDef, step, choice)
  }
}