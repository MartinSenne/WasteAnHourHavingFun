package org.somuchfun.rockpaperscissors.ui

import org.somuchfun.rockpaperscissors._

class ConsoleUI(val game: Game) {
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

  def view(report: Report) = {
    println()
    println(s"${withLength(20, report.playerA.name)} ${withLength(20, report.playerB.name)} Score")
    println( "-----------------------------------------------")

    report.completedRounds.foreach(round ⇒ {
      println(s"${withLength(20,symbol(round.a))} ${withLength(20, symbol(round.b))} ${score(round.result)}")
    })
    if (report.isFinished == true) {
      println( "===============================================")
      val winnerName = report.score.winner match {
        case PlayerIdA => report.playerA.name
        case PlayerIdB => report.playerB.name
      }
      println( s"Winner is ${winnerName}.     Endresult: ${report.score.scoreA}:${report.score.scoreB}" )
    }
  }
}

