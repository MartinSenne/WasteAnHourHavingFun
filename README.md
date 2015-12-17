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

For details on input see below in section (guide).
 
### Execute tests
 
 Start sbt from console via
 
 ```
 sbt
 ```
 
 then from with sbt shell execute
 
 ```
 test
 ``` 
 
 to run all tests.

## Status

* The current implementation runs and 
  * supports different play-modes (computer vs. computer, human vs. computer, human vs. human),
  * obeys the rules of paper-scissors stone and
  * does not allow for a "draw" match, so a match is continued as long as `n` round that are not "draw" have been played. 
  

### Extensibility

* Easy extensibility towards any modulo-based balanced game with an unequal number of weapons. (e.g. rock-paper-scissors-lizard-Spock).  
See (wiki article)[https://en.wikipedia.org/wiki/Rock-paper-scissors#Additional_weapons] for details.
* Easy extensibility towards a real "Client-Server"-based architecture, as `Game` (reflecting the backend)
can be easily adopted to be used from a frontend. In order to do so, wrap parameters for call to `Game.submitMove` and 
result `GameStatus` into appropriate JSON objects. 

### Current Deficiencies

As time is not unlimited, the following deficiencies exist

* Test-wise
  * Test coverage can be improved. Especially the following tests are missing:
    * Test that the final score is correct.  
    * Tests that proof correct behaviour of ConsoleUI.
* Code-wise
  * Nested `if-else` structure in `GameImpl.submitMove` is ugly. This can be easily resolved by turning if statement
   into methods return `Try` and then using for-comprehension to combine the `Try`-monad.
  * `var state` in `Game` is ugly. Two possible approaches to improve that situation
    * Use an actor (and the Akka actor framework) to have the game actor encapsulate its mutable state
    * Use a state monad instead
    

## Guide

After starting the game, you are first required to select the player configuration. 
Select

* `a` for a computer vs computer match
* `b` for a human vs computer match
* `c` for a human vs human match
 
If human players are involved, they are required to enter their name. Please do so to proceed.
 
Game is thus setup, now each round is played asking you for *rock*, *paper* or *scissors*.
 
### Computer vs. computer
 
Simple watch the computers bashing each other.
 
### Human vs. computer
 
After the player configuration has been selected to be human vs. computer and your name was entered,
you can do your moves.
 
* `1` stands for rock
* `2` stands for paper
* `3` stands for scissors
 
Start with entering a number to perform your first move. After doing so, the choice of 
your computer opponent is displayed.
Subsequent entries of your moves will push the game forward, until three rounds have been played,
that were not `draw = 0:0`.
  
In the end, the winner and the final score is presented. An example game-flow is presented below.

### Example game flow


```
Select a: Computer vs. Computer, b: Human vs. Computer, c: Human vs. Human: b
Player name for Player A: Martin

Martin               Computer Watson      Score
-----------------------------------------------
Select from 1: Rock, 2: Paper, 3: Scissors: 1

Martin               Computer Watson      Score
-----------------------------------------------
Rock                 Rock                 0:0
Select from 1: Rock, 2: Paper, 3: Scissors: 1

Martin               Computer Watson      Score
-----------------------------------------------
Rock                 Rock                 0:0
Rock                 Scissors             1:0
Select from 1: Rock, 2: Paper, 3: Scissors: 1

Martin               Computer Watson      Score
-----------------------------------------------
Rock                 Rock                 0:0
Rock                 Scissors             1:0
Rock                 Rock                 0:0
Select from 1: Rock, 2: Paper, 3: Scissors: 1

Martin               Computer Watson      Score
-----------------------------------------------
Rock                 Rock                 0:0
Rock                 Scissors             1:0
Rock                 Rock                 0:0
Rock                 Rock                 0:0
Select from 1: Rock, 2: Paper, 3: Scissors: 1

Martin               Computer Watson      Score
-----------------------------------------------
Rock                 Rock                 0:0
Rock                 Scissors             1:0
Rock                 Rock                 0:0
Rock                 Rock                 0:0
Rock                 Scissors             1:0
Select from 1: Rock, 2: Paper, 3: Scissors: 1

Martin               Computer Watson      Score
-----------------------------------------------
Rock                 Rock                 0:0
Rock                 Scissors             1:0
Rock                 Rock                 0:0
Rock                 Rock                 0:0
Rock                 Scissors             1:0
Rock                 Paper                0:1
-----------------------------------------------
Winner is Martin.
Finalscore                                2:1
```

  



