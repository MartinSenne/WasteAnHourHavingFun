package org.somuchfun.rockpaperscissors

/**
  * Game variant describes properties of 
  * <b>balanced modulo games</b>.
  */
trait GameVariant {
  def n : Int
  def elements: Map[Int, String]

  def beats(a: Int, b: Int) : RoundResult = {
    val rest = (b - a) % n
    val positiveRestMod2 = (if (rest < 0) rest+n else rest) % 2
    val r = ( b-a, positiveRestMod2 )
    r match {
      case (0, _) ⇒ Draw
      case (_, 0) ⇒ PlayerPosA
      case (_, 1) ⇒ PlayerPosB
    }
  }
}

class RegularVariant extends GameVariant {
  override def n = 3

  override val elements = Map (
    1 -> "Rock",
    2 -> "Paper",
    3 -> "Scissors"
  )
}