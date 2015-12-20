package org.somuchfun.rockpaperscissors.ui.impl

import org.somuchfun.rockpaperscissors.{PlayerId, RPSMatch}
import org.somuchfun.rockpaperscissors.ui.util.ConsoleInputUtil._

import scala.io.StdIn

object GenericConsoleViews {
  
  
  def selectAMove(game: RPSMatch): Int = {
    val text = "Select from " + game.variant.elements.map(x ⇒ s"${(x._1)}: ${x._2}").mkString(", ") + ": "
    userNumberSelection(text, 1, game.variant.n)
  }

  def selectGameType(): String = {
    userAlternativesSelection("Select a: Computer vs. Computer, b: Human vs. Computer, c: Human vs. Human: ", Seq("a", "b", "c"))
  }

  def selectPlayerName(playerDef: PlayerId): String = {
    print(s"Player name for Player ${playerDef}: ")
    StdIn.readLine()
  }
}
