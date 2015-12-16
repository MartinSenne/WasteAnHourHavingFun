package org.somuchfun.rockpaperscissors.ui

import org.somuchfun.rockpaperscissors.Game

class ConsoleUI(val game: Game) {
  private def symbol(choice: Int) : String = {
    game.variant.elements(choice)
  }

  private def visual(x: Option[Int]) : String = {
    x match {
      case Some(x) => symbol(x)
      case None => "-"
    }
  }

  private def spaces(length: Int) : String = {
    Iterator.fill(length)(' ').mkString("")
  }

  private def withLength(l: Int, x: String) : String = {
    val i = x.length
    if (i < l) {x + spaces(l-i)} else { x.substring(0, l)}
  }

  def gameStat = {
    println()
    println(s"Round      ${withLength(20, game.playerA.name)} ${withLength(20, game.playerB.name)}")
    println( "==================================================")

    game.moves.foreach( x ⇒ {
      println(s"${withLength(10, x._1.toString)} ${withLength(20,visual(x._2.a))} ${withLength(20, visual(x._2.b))}")
    })
  }
}