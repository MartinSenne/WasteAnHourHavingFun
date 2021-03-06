package org.somuchfun.rockpaperscissors.ui.views.impl.console.util

import org.somuchfun.rockpaperscissors.{Draw, PlayerIdA, PlayerIdB, RoundResult}

object ConsoleFormatting {
  
  private def spaces(length: Int): String = {
    Iterator.fill(length)(' ').mkString("")
  }

  def withLength(l: Int, x: String): String = {
    val i = x.length
    if (i < l) {
      x + spaces(l - i)
    } else {
      x.substring(0, l)
    }
  }

  def score(result: RoundResult) = {
    result match {
      case PlayerIdA ⇒ "1:0"
      case PlayerIdB ⇒ "0:1"
      case Draw ⇒ "0:0"
    }
  }
}
