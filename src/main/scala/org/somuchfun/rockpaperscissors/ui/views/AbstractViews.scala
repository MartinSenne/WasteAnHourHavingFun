package org.somuchfun.rockpaperscissors.ui.views

import org.somuchfun.rockpaperscissors.{PlayerDescription, PlayerId, Report}

object AbstractViews {

  trait ReportView {
    def show(report: Report, playerAName: String, playerBName: String, elements: Map[Int, String]): Unit
  }

  trait SelectPlayerNameView {
    def selectPlayerName(playerDef: PlayerId): String
  }

  trait SelectGameTypeView {
    val types: Map[String, String] = Map(
      "a" -> "Computer vs. Computer",
      "b" -> "Human vs. Computer",
      "c" -> "Human vs. Human"
    )

    def selectGameType(): String
  }

  trait PlayerMoveView {
    def showMove(playerDescription: PlayerDescription, computerChoice: Int, elements: Map[Int, String]) : Unit

    def selectMove(playerDescription: PlayerDescription, elements: Map[Int, String]): Int
  }
}