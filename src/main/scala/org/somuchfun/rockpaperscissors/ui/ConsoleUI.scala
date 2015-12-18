package org.somuchfun.rockpaperscissors.ui

import org.somuchfun.rockpaperscissors._

import scala.io.StdIn
import scala.util.Try

class ConsoleUI(val game: Game) {
  private def symbol(choice: Int) : String = {
    game.variant.elements(choice)
  }

  private def visual(x: Option[Int]) : String = {
    x match {
      case Some(x) ⇒ symbol(x)
      case None ⇒ "-"
    }
  }

  private def spaces(length: Int) : String = {
    Iterator.fill(length)(' ').mkString("")
  }

  private def withLength(l: Int, x: String) : String = {
    val i = x.length
    if (i < l) {x + spaces(l-i)} else { x.substring(0, l)}
  }

  def score(result: RoundResult) = {
    result match {
      case PlayerIdA ⇒ "1:0"
      case PlayerIdB ⇒ "0:1"
      case Draw ⇒ "0:0" 
    }
  }
  
  private def finalScoreAndWinner( gameStatus: GameReport) = {
    val scoreA = gameStatus.completedRounds.filter(cm ⇒ cm.result.equals(PlayerIdA) ).size
    val scoreB = gameStatus.completedRounds.filter(cm ⇒ cm.result.equals(PlayerIdB) ).size
    val winner = if (scoreA > scoreB) PlayerIdA else {if (scoreB > scoreA) PlayerIdB else Draw}
    val s = winner match {
      case PlayerIdA ⇒ s"Winner is ${gameStatus.playerA.name}."
      case PlayerIdB ⇒ s"Winner is ${gameStatus.playerB.name}."
      case Draw ⇒ "Match is draw." // should never happen
    }
    s"${s}\n${withLength(41, "Finalscore")} ${scoreA.toInt}:${scoreB.toInt}"
  }

  def view(gameStatus: GameReport) = {
    println()
    println(s"${withLength(20, gameStatus.playerA.name)} ${withLength(20, gameStatus.playerB.name)} Score")
    println( "-----------------------------------------------")

    gameStatus.completedRounds.foreach(round ⇒ {
      println(s"${withLength(20,symbol(round.a))} ${withLength(20, symbol(round.b))} ${score(round.result)}")
    })
    if (gameStatus.currentStep.equals(None)) {
      println( "-----------------------------------------------")
      println( finalScoreAndWinner( gameStatus ) )
    }
  }
}

object ConsoleUI {
  def userChoiceForMove(game: Game): Int = {
    val text = "Select from " + game.variant.elements.map( x ⇒ s"${(x._1)}: ${x._2}" ).mkString(", ") + ": "
    userNumberSelect( text, 1, game.variant.n)
  }
  
  def selectGameType() : String = {
    userStringSelect("Select a: Computer vs. Computer, b: Human vs. Computer, c: Human vs. Human: ", Seq("a", "b", "c"))
  }
  
  def queryPlayerName( playerDef: PlayerId ): String = {
    print(s"Player name for Player ${playerDef}: ")
    StdIn.readLine()
  }

  def userNumberSelect(text: String, min: Int, max : Int): Int = {
    
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

  def userStringSelect(text: String, strings: Seq[String]): String = {
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