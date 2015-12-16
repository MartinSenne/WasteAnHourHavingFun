package org.somuchfun.rockpaperscissors

import org.somuchfun.rockpaperscissors.players._
import org.somuchfun.rockpaperscissors.ui.ConsoleUI



trait PlayerDef
trait Winning
case object A extends PlayerDef with Winning
case object B extends PlayerDef with Winning
case object Draw extends Winning


class Game(val variant: GameVariant, val playerA: Player, val playerB: Player, val rounds: Int) {
  case class Move( a: Option[Int], b: Option[Int] )
  
  var moves : Map[Int, Move] = ( 1 to rounds ).map(x ⇒ (x, Move(None, None) ) ).toMap
  
  def submitMove(playerDef: PlayerDef, step: Int, choice: Int) : Unit = {
    val moveAtStep = moves(step)
    val newMove = playerDef match {
      case A => moveAtStep.copy(a=Some(choice))
      case B => moveAtStep.copy(b=Some(choice))
    }
    moves = moves.updated( step, newMove )
  }
  
  def play: Unit = {
    val ui = new ConsoleUI(this)
    ui.gameStat
    for (i <- 1 to rounds) {
      playerA.triggerMove(this, i)
      playerB.triggerMove(this, i)
      ui.gameStat
    }
  }
}



object Main {
  def main(args: Array[String]) : Unit = {
    val playerA = new HumanPlayer("Horst", A)
    val playerB = new HumanPlayer("Beate", B)
    // val playerA = new ComputerPlayer(A)
    // val playerB = new ComputerPlayer(B)
    val game = new Game( new RegularVariant, playerA, playerB, 3 )
    game.play
  }
}
