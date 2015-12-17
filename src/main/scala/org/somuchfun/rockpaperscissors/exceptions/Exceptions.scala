package org.somuchfun.rockpaperscissors.exceptions

class MoveAlreadyMadeException(message: String = null, cause: Throwable = null) extends IllegalArgumentException(message, cause)
class IncorrectRoundNumberException(message: String = null, cause: Throwable = null) extends IllegalArgumentException(message, cause)
class GameAlreadyFinishedException(message: String = null, cause: Throwable = null) extends IllegalArgumentException(message, cause)
