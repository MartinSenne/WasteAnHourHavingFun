package org.somuchfun.rockpaperscissors.ui.impl

import org.somuchfun.rockpaperscissors._
import org.somuchfun.rockpaperscissors.ui.ReportView
import org.somuchfun.rockpaperscissors.ui.util.ConsoleFormatting


class ConsoleReportView extends ReportView {
  import ConsoleFormatting._

  override def show(report: Report, playerAName: String, playerBName: String, elements: Map[Int, String]) = {
    println()
    println(s"${withLength(20, playerAName)} ${withLength(20, playerBName)} Score")
    println( "-----------------------------------------------")

    report.completedRounds.foreach(round ⇒ {
      println(s"${withLength(20, elements(round.a))} ${withLength(20, elements(round.b))} ${score(round.result)}")
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

