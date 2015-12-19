package org.somuchfun.rockpaperscissors.ui

import org.somuchfun.rockpaperscissors.{PlayerId, Game}

import scala.io.StdIn
import scala.util.Try

object ConsoleInput {
  def selectAMove(game: Game): Int = {
    val text = "Select from " + game.variant.elements.map( x ⇒ s"${(x._1)}: ${x._2}" ).mkString(", ") + ": "
    userNumberSelection(text, 1, game.variant.n)
  }

  def selectGameType() : String = {
    userAlternativesSelection("Select a: Computer vs. Computer, b: Human vs. Computer, c: Human vs. Human: ", Seq("a", "b", "c"))
  }

  def selectPlayerName(playerDef: PlayerId ): String = {
    print(s"Player name for Player ${playerDef}: ")
    StdIn.readLine()
  }

  def userNumberSelection(text: String, min: Int, max : Int): Int = {
    var result: Try[Int] = null
    do {
      result = Try {
        print(text)
        val choice = StdIn.readLine().toInt
        if ((min <= choice) && (choice <= max)) choice else throw new IllegalArgumentException("Input not in range.")
      }
    } while (result.isFailure)

    result.get
  }

  def userAlternativesSelection(text: String, strings: Seq[String]): String = {
    var result: Try[String] = null
    do {
      result = Try {
        print(text)
        val input = StdIn.readLine()
        if (strings.contains(input)) input else throw new IllegalArgumentException("Input not allowed.")
      }
    } while (result.isFailure)

    result.get
  }
}