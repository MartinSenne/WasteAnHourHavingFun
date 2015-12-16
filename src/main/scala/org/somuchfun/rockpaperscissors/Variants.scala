package org.somuchfun.rockpaperscissors

trait GameVariant {
  def n : Int
  def elements: Map[Int, String]

  def beats(a: Int, b: Int) : Winning = {
    // balanced modulo games
    val rest = (b - a) % n
    val positiveRestMod2 = (if (rest < 0) rest+n else rest) % 2
    val r = ( b-a, positiveRestMod2 )
    r match {
      case (0, _) ⇒ Draw
      case (_, 0) ⇒ A
      case (_, 1) ⇒ B
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