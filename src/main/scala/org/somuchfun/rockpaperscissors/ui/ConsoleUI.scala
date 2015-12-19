package org.somuchfun.rockpaperscissors.ui

import org.somuchfun.rockpaperscissors._

class ConsoleUI(val game: RPSMatch) {
  private def symbol(choice: Int) : String = {
    game.variant.elements(choice)
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

  def view(report: Report, playerAName: String, playerBName: String) = {
    println()
    println(s"${withLength(20, playerAName)} ${withLength(20, playerBName)} Score")
    println( "-----------------------------------------------")

    report.completedRounds.foreach(round ⇒ {
      println(s"${withLength(20,symbol(round.a))} ${withLength(20, symbol(round.b))} ${score(round.result)}")
    })
    if (report.isFinished == true) {
      println( "===============================================")
      val winnerName =  report.score.winner match {
        case PlayerIdA => playerAName
        case PlayerIdB => playerBName
      }
      println( s"Winner is ${winnerName}.     Endresult: ${report.score.scoreA}:${report.score.scoreB}" )
    }
  }
}

