package org.somuchfun.rockpaperscissors.ui.views.impl.console

import org.somuchfun.rockpaperscissors._
import org.somuchfun.rockpaperscissors.ui.views.impl.console.util.ConsoleFormatting
import org.somuchfun.rockpaperscissors.ui.views.AbstractViews._
import org.somuchfun.rockpaperscissors.ui.views.impl.console.util.ConsoleInputUtil._

import scala.io.StdIn

object ConsoleViews {

  class ConsoleReportView extends ReportView {

    import ConsoleFormatting._

    override def show(report: Report, playerAName: String, playerBName: String, elements: Map[Int, String]) = {
      println()
      println(s"${withLength(20, playerAName)} ${withLength(20, playerBName)} Score")
      println("-----------------------------------------------")

      report.completedRounds.foreach(round ⇒ {
        println(s"${withLength(20, elements(round.a))} ${withLength(20, elements(round.b))} ${score(round.result)}")
      })
      if (report.isFinished == true) {
        println("===============================================")
        val winnerName = report.score.winner match {
          case PlayerIdA => playerAName
          case PlayerIdB => playerBName
        }
        println(s"Winner is ${winnerName}.     Endresult: ${report.score.scoreA}:${report.score.scoreB}")
      }
    }
  }

  class ConsoleSelectGameTypeView extends SelectGameTypeView {
    def selectGameType(): String = {
      val text = "Select gametype: " + types.map(x => s"${x._1}: ${x._2}").mkString(", ")
      userAlternativesSelection(text, types.keySet.toSeq)
    }
  }

  class ConsoleSelectPlayerNameView extends SelectPlayerNameView {
    def selectPlayerName(playerDef: PlayerId): String = {
      print(s"Player name for Player ${playerDef}: ")
      StdIn.readLine()
    }
  }

  class ConsolePlayerMoveView(val elements: Map[Int, String]) extends PlayerMoveView {
    private def title(pd: PlayerDescription) : String = {
      s"Player ${pd.playerId} (${pd.name}}"
    }
    
    def selectMove(pd: PlayerDescription): Int = {
      val n = elements.size
      val text = s" ${title(pd)}: Please select from " + elements.map(x ⇒ s"${x._1}: ${x._2}").mkString(", ") + ": "
      userNumberSelection(text, 1, n)
    }

    override def showMove(pd: PlayerDescription, computerChoice: Int): Unit = {
      println( s"{title(pd)} made choice ${elements(computerChoice)}" )
    }
  }
}
  

  

