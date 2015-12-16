# WasteAnHourHavingFun
An implementation of **Rock Paper Scissors** in Scala (Console app) without using any external libraries or frameworks.

## Build

Clone the repo [WasteAnHourHavingFun](https://github.com/MartinSenne/WasteAnHourHavingFun) from GitHub via

```
git clone git@github.com:MartinSenne/WasteAnHourHavingFun.git
```

Install sbt according to [sbt manual installation]([http://www.scala-sbt.org/release/tutorial/Manual-Installation.html)


### Run the game (Execute)

Start sbt from console via

```
sbt
```

then from with sbt shell execute

```
run
``` 

## Status

* The current implementation runs and 
  * supports different play-modes (computer vs. computer, human vs. computer, human vs. human),
  * obeys the rules of paper-scissors stone and
  * does not allow for a "draw" match, so if a match is continued as long as `n` round that are not "draw" have been played. 
  

### Extensibility

* Easy extensibility towards any modulo-based balanced game with an unequal number of weapons. (e.g. rock-paper-scissors-lizard-Spock).  
See (wiki article)[https://en.wikipedia.org/wiki/Rock-paper-scissors#Additional_weapons] for details.
* Easy extensibility towards a real "Client-Server"-based architecture, as `Game` (reflecting the backend)
can be easily adopted to be used from the frontend-side. In order to do so, wrap parameters for call to `Game.submitMove` and 
result `GameStatus` into appropriate JSON objects. 

### Current Deficiencies

As time is not unlimited, the following deficiencies exist

* Test-wise
  * Tests are basically not present. Therefor, at least the following tests need to be added:
    * Test that a game does end properly after `n` rounds (being not draw)
      * Generate sequence of moves in advance, apply them via mocked `MockPlayer`s and check result.
    * Test the the final score is correct.  
    * Test that invalid input to `Game.submitMove` is handled properly.
      * Implementation: Use an player mock `InvalidPlayer` that calls `submitMove` with invalid parameters.
    * Tests that proof correct behaviour of ConsoleUI.
* Code-wise
  * Nested `if-else` structure in `GameImpl.submitMove` is ugly. This can be easily resolved by turning if statement
   into methods return `Try` and then using for-comprehension to combine the `Try`-monad.
  * `var state` in `Game` is ugly. Two possible approaches to improve that situation
    * Use an actor (and the Akka actor framework) to have the game actor encapsulate its mutable state
    * Use a state monad instead
    


  



